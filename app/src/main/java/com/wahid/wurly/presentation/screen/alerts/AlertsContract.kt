package com.wahid.wurly.presentation.screen.alerts

import androidx.annotation.DrawableRes
import com.wahid.wurly.presentation.common.model.WeatherAlertItem

/**
 * Duration options for alert active period.
 */
enum class AlertDuration { OneHour, SixHours, TwentyFourHours }

/**
 * Notification style options.
 */
enum class NotificationStyle { Standard, Alarm }

/**
 * Underlying style for an active scheduled alert.
 */
enum class AlertStyle { Notification, Alarm }

/** Alert types the user can subscribe to. */
enum class AlertCase { Storm, HeatAdvisory, FlashFloodWatch, CustomTemperature }

/**
 * Represents the complete UI state for the Weather Alerts screen.
 */
sealed interface AlertsUiState {
    data object Loading : AlertsUiState

    data class Success(
        val alertsEnabled: Boolean,
        val showAddAlertSheet: Boolean,
        val pendingCase: AlertCase,
        val pendingDuration: AlertDuration,
        val pendingStyle: NotificationStyle,
        val pendingTemperature: Int,
        val activeAlerts: List<WeatherAlertItem>,
        @param:DrawableRes val currentBackground: Int,
    ) : AlertsUiState

    data class Error(val message: String) : AlertsUiState
}

/**
 * One-shot events emitted from the Weather Alerts screen UI.
 */
sealed interface AlertsUiEvent {
    data object OnOpenAddAlertSheet : AlertsUiEvent
    data object OnDismissAddAlertSheet : AlertsUiEvent
    data class OnPendingCaseChange(val alertCase: AlertCase) : AlertsUiEvent
    data class OnPendingDurationChange(val duration: AlertDuration) : AlertsUiEvent
    data class OnPendingStyleChange(val style: NotificationStyle) : AlertsUiEvent
    data class OnPendingTemperatureChange(val temperature: Int) : AlertsUiEvent
    data object OnConfirmAddAlert : AlertsUiEvent
    data class OnToggleAlerts(val enabled: Boolean) : AlertsUiEvent
    data class OnRemoveAlert(val id: String) : AlertsUiEvent
}