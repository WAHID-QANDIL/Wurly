package com.wahid.wurly.presentation.screen.alerts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Warning
import androidx.lifecycle.ViewModel
import com.wahid.wurly.R
import com.wahid.wurly.presentation.common.model.WeatherAlertItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<AlertsUiState>(
        AlertsUiState.Success(
            alertsEnabled = true,
            activeDuration = AlertDuration.SixHours,
            notificationStyle = NotificationStyle.Standard,
            activeAlerts = sampleAlerts(),
            currentBackground = R.drawable.image1,
        )
    )
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    fun onEvent(event: AlertsUiEvent) {
        when (event) {
            is AlertsUiEvent.OnBackClick -> { /* Handle navigation back */ }
            is AlertsUiEvent.OnAddAlertClick -> { /* Handle add alert */ }
            is AlertsUiEvent.OnNavItemClick -> { /* Handle nav item click */ }

            is AlertsUiEvent.OnToggleAlerts -> {
                updateSuccess { it.copy(alertsEnabled = event.enabled) }
            }

            is AlertsUiEvent.OnDurationChange -> {
                updateSuccess { it.copy(activeDuration = event.duration) }
            }

            is AlertsUiEvent.OnNotificationStyleChange -> {
                updateSuccess { it.copy(notificationStyle = event.style) }
            }

            is AlertsUiEvent.OnRemoveAlert -> {
                updateSuccess { state ->
                    state.copy(
                        activeAlerts = state.activeAlerts.filter { it.id != event.id }
                    )
                }
            }
        }
    }

    private inline fun updateSuccess(
        transform: (AlertsUiState.Success) -> AlertsUiState.Success,
    ) {
        _uiState.update { current ->
            if (current is AlertsUiState.Success) transform(current) else current
        }
    }

    private fun sampleAlerts() = listOf(
        WeatherAlertItem(
            id = "1",
            title = "Storm Warning",
            description = "Heavy thunderstorm expected in your area",
            icon = Icons.Filled.Warning,
            timestamp = "2 hours ago",
        ),
        WeatherAlertItem(
            id = "2",
            title = "Heat Advisory",
            description = "Temperatures exceeding 40°C expected",
            icon = Icons.Filled.Thermostat,
            timestamp = "5 hours ago",
        ),
        WeatherAlertItem(
            id = "3",
            title = "Flash Flood Watch",
            description = "Possible flooding in low-lying areas",
            icon = Icons.Filled.NotificationsActive,
            timestamp = "1 day ago",
        ),
    )
}