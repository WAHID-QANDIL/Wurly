package com.wahid.wurly.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahid.wurly.R
import com.wahid.wurly.domain.model.settings.SupportedLanguage
import com.wahid.wurly.domain.model.settings.UserSettings
import com.wahid.wurly.domain.model.weather.WeatherUnit
import com.wahid.wurly.domain.model.weather.WindUnit
import com.wahid.wurly.domain.usecase.GetUserSettings
import com.wahid.wurly.domain.usecase.UpdateUserSettings
import com.wahid.wurly.utils.ResourceAccessor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserSettings: GetUserSettings,
    private val updateUserSettings: UpdateUserSettings,
    private val resourceAccessor: ResourceAccessor,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingsUiState>(
        SettingsUiState.Success(
            useGps = true,
            selectedTemperatureUnit = TemperatureUnit.Celsius,
            selectedWindSpeedUnit = WindSpeedUnit.Ms,
            selectedLanguage = AppLanguage.EN,
            currentLanguageDisplayName = localizedLanguageName(AppLanguage.EN),
            currentBackground = R.drawable.image1,
        )
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private var currentSettings: UserSettings = UserSettings.default()

    init {
        viewModelScope.launch {
            getUserSettings().collectLatest { settings ->
                currentSettings = settings
                val tempUnit = when (settings.weatherUnit) {
                    WeatherUnit.METRIC -> TemperatureUnit.Celsius
                    WeatherUnit.IMPERIAL -> TemperatureUnit.Fahrenheit
                }
                val windUnit = when (settings.windUnit) {
                    WindUnit.METER_PER_SECOND -> WindSpeedUnit.Ms
                    WindUnit.MILE_PER_HOUR -> WindSpeedUnit.Mph
                }
                val appLanguage = when (settings.language.uppercase()) {
                    SupportedLanguage.ARABIC.code -> AppLanguage.AR
                    else -> AppLanguage.EN
                }

                updateSuccess {
                    it.copy(
                        selectedTemperatureUnit = tempUnit,
                        selectedWindSpeedUnit = windUnit,
                        selectedLanguage = appLanguage,
                        currentLanguageDisplayName = localizedLanguageName(appLanguage),
                    )
                }
            }
        }
    }

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.OnToggleGps -> {
                updateSuccessPersist { ui ->
                    ui.copy(useGps = event.enabled)
                }
            }

            is SettingsUiEvent.OnTemperatureUnitChange -> {
                updateSuccessPersist { ui ->
                    ui.copy(selectedTemperatureUnit = event.unit)
                }
            }

            is SettingsUiEvent.OnWindSpeedUnitChange -> {
                updateSuccessPersist { ui ->
                    ui.copy(selectedWindSpeedUnit = event.unit)
                }
            }

            is SettingsUiEvent.OnLanguageChange -> {
                updateSuccessPersist { ui ->
                    ui.copy(
                        selectedLanguage = event.language,
                        currentLanguageDisplayName = localizedLanguageName(event.language),
                    )
                }
            }
        }
    }

    private fun localizedLanguageName(language: AppLanguage): String = when (language) {
        AppLanguage.EN -> resourceAccessor.getString(R.string.settings_language_name_english)
        AppLanguage.AR -> resourceAccessor.getString(R.string.settings_language_name_arabic)
    }

    private inline fun updateSuccess(
        transform: (SettingsUiState.Success) -> SettingsUiState.Success,
    ) {
        _uiState.update { current ->
            if (current is SettingsUiState.Success) transform(current) else current
        }
    }

    private inline fun updateSuccessPersist(
        transform: (SettingsUiState.Success) -> SettingsUiState.Success,
    ) {
        val updated = (_uiState.value as? SettingsUiState.Success)?.let(transform) ?: return
        _uiState.value = updated
        persistSettings(updated)
    }

    private fun persistSettings(ui: SettingsUiState.Success) {
        val updatedDomain = currentSettings.copy(
            weatherUnit = when (ui.selectedTemperatureUnit) {
                TemperatureUnit.Celsius -> WeatherUnit.METRIC
                TemperatureUnit.Fahrenheit -> WeatherUnit.IMPERIAL
                TemperatureUnit.Kelvin -> WeatherUnit.METRIC
            },
            windUnit = when (ui.selectedWindSpeedUnit) {
                WindSpeedUnit.Ms -> WindUnit.METER_PER_SECOND
                WindSpeedUnit.Mph -> WindUnit.MILE_PER_HOUR
            },
            language = when (ui.selectedLanguage) {
                AppLanguage.EN -> SupportedLanguage.ENGLISH.code
                AppLanguage.AR -> SupportedLanguage.ARABIC.code
            },
            isLocationEnabled = ui.useGps,
            isCached = false,
        )
        currentSettings = updatedDomain
        viewModelScope.launch { updateUserSettings(updatedDomain) }
    }
}