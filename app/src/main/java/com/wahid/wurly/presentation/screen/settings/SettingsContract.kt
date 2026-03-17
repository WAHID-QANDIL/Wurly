package com.wahid.wurly.presentation.screen.settings

import androidx.annotation.DrawableRes

/**
 * Temperature unit options.
 */
enum class TemperatureUnit { Celsius, Fahrenheit, Kelvin }

/**
 * Wind speed unit options.
 */
enum class WindSpeedUnit { Ms, Mph }

/**
 * App language options.
 */
enum class AppLanguage { EN, AR }

/**
 * Represents the complete UI state for the Settings screen.
 */
sealed interface SettingsUiState {
    data object Loading : SettingsUiState

    data class Success(
        val useGps: Boolean,
        val selectedTemperatureUnit: TemperatureUnit,
        val selectedWindSpeedUnit: WindSpeedUnit,
        val selectedLanguage: AppLanguage,
        val currentLanguageDisplayName: String,
        @param:DrawableRes val currentBackground: Int,
    ) : SettingsUiState

    data class Error(val message: String) : SettingsUiState
}

/**
 * One-shot events emitted from the Settings screen UI.
 */
sealed interface SettingsUiEvent {
    data class OnToggleGps(val enabled: Boolean) : SettingsUiEvent
    data class OnTemperatureUnitChange(val unit: TemperatureUnit) : SettingsUiEvent
    data class OnWindSpeedUnitChange(val unit: WindSpeedUnit) : SettingsUiEvent
    data class OnLanguageChange(val language: AppLanguage) : SettingsUiEvent
}