package com.wahid.wurly.presentation.screen.home

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahid.wurly.R
import com.wahid.wurly.domain.model.settings.UserSettings
import com.wahid.wurly.domain.model.weather.WeatherUnit
import com.wahid.wurly.domain.model.weather.WindUnit
import com.wahid.wurly.domain.usecase.GetCurrentWeather
import com.wahid.wurly.domain.usecase.GetDayForecast
import com.wahid.wurly.domain.usecase.SettingsUseCases
import com.wahid.wurly.presentation.common.model.ForecastCardItem
import com.wahid.wurly.presentation.framwork.location.LocationServiceProvider
import com.wahid.wurly.presentation.screen.home.component.WeatherDetailItem
import com.wahid.wurly.utils.Constants.METERS_PER_KM
import com.wahid.wurly.utils.Constants.METERS_PER_MILE
import com.wahid.wurly.utils.Constants.MPS_TO_MPH
import com.wahid.wurly.utils.ExtUtils.toHourLabel
import com.wahid.wurly.utils.ResourceAccessor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

@SuppressLint("DefaultLocale")
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val resourceAccessor: ResourceAccessor,
    private val getDayForecast: GetDayForecast,
    private val getCurrentWeather: GetCurrentWeather,
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {
    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _userSettings: MutableStateFlow<UserSettings> by lazy {
        MutableStateFlow(UserSettings.default())
    }
    private val userSettings = _userSettings.asStateFlow()

    private var fetchJob: Job? = null

    init {
        viewModelScope.launch {
            settingsUseCases.getUserSettings().collectLatest { settings ->
                _userSettings.value = settings
                fetchHomeWeather(isRefresh = true)
            }
        }
    }

    private fun fetchHomeWeather(isRefresh: Boolean = false) {

        fetchJob?.cancel()

        if (isRefresh) {
            setRefreshing(true)
        }

        fetchJob = viewModelScope.launch {

            val location = LocationServiceProvider.getLastLocation()
            if (location == null) {
                _uiState.update { state ->
                    when (state) {
                        is HomeUiState.Success -> state.copy(isRefreshing = false)
                        else -> HomeUiState.Error("Failed to get location")
                    }
                }
                return@launch
            }

            val params = mutableMapOf<String, String>().apply {
                this["lat"] = location.latitude.toString()
                this["lon"] = location.longitude.toString()
                this["cnt"] = "${5 * 7}"
                this["units"] = userSettings.value.weatherUnit.unit
                this["lang"] = userSettings.value.language.lowercase(Locale.ROOT)
            }


            val forecastFlow = getDayForecast(params, isFavorite = false)
            val currentFlow = getCurrentWeather(params)

            combine(forecastFlow, currentFlow) { forecast, current ->
                forecast to current
            }
                .onEach {
                    _uiState.update { state ->
                        if (state is HomeUiState.Success) state.copy(
                            isRefreshing = false
                        ) else state
                    }
                    setRefreshing(false)
                }
                .catch {
                    setRefreshing(false)
                    _uiState.value = HomeUiState.Error(it.message ?: "Error")
                }
                .collect { (forecast, currentWeather) ->
                    // settings reflected below for wind/visibility formatting

                    val settings = userSettings.value
                    val windSpeedValueMph = if (settings.weatherUnit == WeatherUnit.IMPERIAL) {
                        currentWeather.windSpeed
                    } else {
                        currentWeather.windSpeed * MPS_TO_MPH
                    }
                    val windGustValueMph = if (settings.weatherUnit == WeatherUnit.IMPERIAL) {
                        currentWeather.windGust
                    } else {
                        currentWeather.windGust * MPS_TO_MPH
                    }

                    val windSpeedDisplay = when (settings.windUnit) {
                        WindUnit.MILE_PER_HOUR -> resourceAccessor.getString(
                            R.string.weather_unit_mph,
                            String.format("%.1f", windSpeedValueMph)
                        )

                        WindUnit.METER_PER_SECOND -> resourceAccessor.getString(
                            R.string.weather_unit_ms,
                            String.format("%.1f", currentWeather.windSpeed)
                        )
                    }
                    val windGustDisplay = when (settings.windUnit) {
                        WindUnit.MILE_PER_HOUR -> resourceAccessor.getString(
                            R.string.weather_unit_mph,
                            String.format("%.1f", windGustValueMph)
                        )

                        WindUnit.METER_PER_SECOND -> resourceAccessor.getString(
                            R.string.weather_unit_ms,
                            String.format("%.1f", currentWeather.windGust)
                        )
                    }

                    val visibilityDisplay = when (settings.weatherUnit) {
                        WeatherUnit.IMPERIAL -> resourceAccessor.getString(
                            R.string.weather_unit_mi,
                            String.format(
                                "%.1f",
                                currentWeather.visibility / METERS_PER_MILE
                            )
                        )

                        WeatherUnit.METRIC -> resourceAccessor.getString(
                            R.string.weather_unit_km,
                            String.format(
                                "%.1f",
                                currentWeather.visibility / METERS_PER_KM
                            )
                        )
                    }
                    val humidityDisplay = resourceAccessor.getString(
                        R.string.weather_unit_percent,
                        String.format("%.0f", currentWeather.humidity)
                    )

                    val feelsLikeSubtitle = resourceAccessor.getString(
                        R.string.weather_subtitle_feels_like,
                        String.format("%.0f", currentWeather.feelsLike)
                    )

                    val windGustSubtitle = resourceAccessor.getString(
                        R.string.weather_subtitle_gusts,
                        windGustDisplay
                    )

                    val pressureDisplay = resourceAccessor.getString(
                        R.string.weather_unit_hpa,
                        String.format("%.0f", currentWeather.pressure)
                    )

                    val pressureSeaLevelSubtitle = resourceAccessor.getString(
                        R.string.weather_subtitle_pressure_sea_level,
                        resourceAccessor.getString(
                            R.string.weather_unit_hpa,
                            String.format("%.0f", currentWeather.seaLevel)
                        )
                    )
                    val currentDate = Instant.ofEpochSecond(currentWeather.dayTime)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

                    val forecastCards = forecast.list
                        .filter {
                            Instant.ofEpochSecond(it.dayTime)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate() == currentDate
                        }
                        .take(8)
                        .map { forecastItem ->

                            val tempLabel = "${forecastItem.temp.roundToInt()}°"
                            val humidityLabel = resourceAccessor.getString(
                                R.string.forecast_card_subtitle_humidity,
                                String.format("%.0f%%", forecastItem.humidity)
                            )
                            ForecastCardItem(
                                timeLabel = forecastItem.dayTime.toHourLabel(),
                                temperature = tempLabel,
                                subtitle = humidityLabel,
                                icon = forecastItem.condition.icon,
                                description = forecastItem.condition.description
                            )
                        }
                    _uiState.value = HomeUiState.Success(
                        weather = currentWeather.condition,
                        locationName = "${currentWeather.cityName }, ${currentWeather.sys.country}",
                        dateTime = currentWeather.dayTimeString,
                        temperature = "${currentWeather.temp.roundToInt()}",
                        condition = currentWeather.condition.description,
                        isRefreshing = false,
                        locationEnabled = true,
                        forecastCards = forecastCards,
                        currentBackground = R.drawable.image1,
                        details = listOf(
                            WeatherDetailItem(
                                icon = Icons.Outlined.WaterDrop,
                                iconContentDescriptionRes = R.string.weather_icon_humidity_cd,
                                labelRes = R.string.weather_label_humidity,
                                value = humidityDisplay,
                                subtitle = feelsLikeSubtitle,
                            ),
                            WeatherDetailItem(
                                icon = Icons.Outlined.Air,
                                iconContentDescriptionRes = R.string.weather_icon_wind_cd,
                                labelRes = R.string.weather_label_wind,
                                value = windSpeedDisplay,
                                subtitle = windGustSubtitle,
                            ),
                            WeatherDetailItem(
                                icon = Icons.Outlined.Visibility,
                                iconContentDescriptionRes = R.string.weather_icon_visibility_cd,
                                labelRes = R.string.weather_label_visibility,
                                value = visibilityDisplay,
                                subtitle = resourceAccessor.getString(R.string.weather_subtitle_visibility_clear),
                            ),
                            WeatherDetailItem(
                                icon = Icons.Outlined.Speed,
                                iconContentDescriptionRes = R.string.weather_icon_pressure_cd,
                                labelRes = R.string.weather_label_pressure,
                                value = pressureDisplay,
                                subtitle = pressureSeaLevelSubtitle,
                            ),
                        )
                    )
                }
        }
    }

    private fun setRefreshing(isRefreshing: Boolean) {
        _uiState.update { state ->
            if (state is HomeUiState.Success) state.copy(isRefreshing = isRefreshing) else state
        }
    }

    fun onAction(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.OnRefresh -> fetchHomeWeather(isRefresh = true)
        }
    }

}