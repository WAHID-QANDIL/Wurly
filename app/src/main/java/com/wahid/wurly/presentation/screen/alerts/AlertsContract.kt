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
 * Represents the complete UI state for the Weather Alerts screen.
 */
sealed interface AlertsUiState {
    data object Loading : AlertsUiState

    data class Success(
        val alertsEnabled: Boolean,
        val activeDuration: AlertDuration,
        val notificationStyle: NotificationStyle,
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
    data object OnAddAlertClick : AlertsUiEvent
    data class OnToggleAlerts(val enabled: Boolean) : AlertsUiEvent
    data class OnDurationChange(val duration: AlertDuration) : AlertsUiEvent
    data class OnNotificationStyleChange(val style: NotificationStyle) : AlertsUiEvent
    data class OnRemoveAlert(val id: String) : AlertsUiEvent
    data class OnNavItemClick(val index: Int) : AlertsUiEvent
}