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
        /** Controls whether all alerts are globally active. */
        val alertsEnabled: Boolean,
        /** Whether the Add Alert bottom-sheet is visible. */
        val showAddAlertSheet: Boolean,
        /** Alert type selected inside the bottom-sheet (not yet committed). */
        val pendingCase: AlertCase,
        /** Duration selected inside the bottom-sheet (not yet committed). */
        val pendingDuration: AlertDuration,
        /** Style selected inside the bottom-sheet (not yet committed). */
        val pendingStyle: NotificationStyle,
        /** Target temperature (in celsius) selected for custom temperature alerts. */
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
    data object OnBackClick : AlertsUiEvent
    /** Open the Add Alert bottom-sheet. */
    data object OnOpenAddAlertSheet : AlertsUiEvent
    /** Dismiss the Add Alert bottom-sheet without saving. */
    data object OnDismissAddAlertSheet : AlertsUiEvent
    /** User changed the alert-type radio inside the sheet. */
    data class OnPendingCaseChange(val alertCase: AlertCase) : AlertsUiEvent
    /** User changed the duration segment inside the sheet. */
    data class OnPendingDurationChange(val duration: AlertDuration) : AlertsUiEvent
    /** User changed the notification-style radio inside the sheet. */
    data class OnPendingStyleChange(val style: NotificationStyle) : AlertsUiEvent
    /** User changed the target temperature slider. */
    data class OnPendingTemperatureChange(val temperature: Int) : AlertsUiEvent
    /** Confirm button pressed – schedule and persist a new alert. */
    data object OnConfirmAddAlert : AlertsUiEvent
    /** Toggle the global alerts enabled switch. */
    data class OnToggleAlerts(val enabled: Boolean) : AlertsUiEvent
    /** Remove (stop) an active alert by id. */
    data class OnRemoveAlert(val id: String) : AlertsUiEvent
    data class OnNavItemClick(val index: Int) : AlertsUiEvent
}