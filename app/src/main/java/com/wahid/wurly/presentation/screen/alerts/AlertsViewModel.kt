package com.wahid.wurly.presentation.screen.alerts

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Warning
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahid.wurly.R
import com.wahid.wurly.domain.usecase.alerts.DeleteAlert
import com.wahid.wurly.domain.usecase.alerts.GetAlerts
import com.wahid.wurly.domain.usecase.alerts.UpsertAlert
import com.wahid.wurly.presentation.common.model.WeatherAlertItem
import com.wahid.wurly.presentation.screen.alerts.AlertStyle
import com.wahid.wurly.work.WeatherAlertScheduler
import com.wahid.wurly.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val getAlerts: GetAlerts,
    private val upsertAlert: UpsertAlert,
    private val deleteAlert: DeleteAlert,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AlertsUiState>(
        AlertsUiState.Success(
            alertsEnabled = true,
            showAddAlertSheet = false,
            pendingCase = AlertCase.Storm,
            pendingDuration = AlertDuration.SixHours,
            pendingStyle = NotificationStyle.Standard,
            pendingTemperature = 20,
            activeAlerts = emptyList(),
            currentBackground = R.drawable.image1,
        )
    )
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.getUserSettings().collectLatest { settings ->
                updateSuccess { it.copy(alertsEnabled = settings.isNotificationsEnabled) }
            }
        }
        viewModelScope.launch {
            getAlerts().collectLatest { alerts ->
                val items = alerts.map { alert ->
                    WeatherAlertItem(
                        id = alert.id.toString(),
                        title = alert.alertCase.toTitle(),
                        description = alert.toDescription(),
                        icon = alert.alertCase.toIcon(),
                        timestamp = formatTimestamp(alert.createdAt),
                    )
                }
                updateSuccess { it.copy(activeAlerts = items) }
            }
        }
    }

    fun onEvent(event: AlertsUiEvent) {
        when (event) {

            is AlertsUiEvent.OnOpenAddAlertSheet -> {
                updateSuccess { it.copy(showAddAlertSheet = true) }
            }

            is AlertsUiEvent.OnDismissAddAlertSheet -> {
                updateSuccess { it.copy(showAddAlertSheet = false) }
            }

            is AlertsUiEvent.OnPendingCaseChange -> {
                updateSuccess { it.copy(pendingCase = event.alertCase) }
            }

            is AlertsUiEvent.OnPendingDurationChange -> {
                updateSuccess { it.copy(pendingDuration = event.duration) }
            }

            is AlertsUiEvent.OnPendingStyleChange -> {
                updateSuccess { it.copy(pendingStyle = event.style) }
            }

            is AlertsUiEvent.OnPendingTemperatureChange -> {
                updateSuccess { it.copy(pendingTemperature = event.temperature) }
            }

            is AlertsUiEvent.OnConfirmAddAlert -> {
                val state = _uiState.value as? AlertsUiState.Success ?: return
                val alertStyle = when (state.pendingStyle) {
                    NotificationStyle.Standard -> AlertStyle.Notification
                    NotificationStyle.Alarm -> AlertStyle.Alarm
                }
                val delayMillis = durationToMillis(state.pendingDuration)

                WeatherAlertScheduler.scheduleAlert(context = appContext)

                viewModelScope.launch {
                    upsertAlert(
                        com.wahid.wurly.domain.model.weather.WeatherAlert(
                            alertCase = state.pendingCase,
                            style = alertStyle,
                            durationMillis = delayMillis,
                            createdAt = System.currentTimeMillis(),
                            enabled = true,
                            targetTemperature = if (state.pendingCase == AlertCase.CustomTemperature) state.pendingTemperature else null
                        )
                    )
                }

                updateSuccess {
                    it.copy(
                        showAddAlertSheet = false,
                        pendingCase = AlertCase.Storm,
                        pendingDuration = AlertDuration.SixHours,
                        pendingStyle = NotificationStyle.Standard,
                        pendingTemperature = 20,
                    )
                }
            }

            is AlertsUiEvent.OnToggleAlerts -> {
                viewModelScope.launch {
                    val currentSettings = settingsRepository.getUserSettings().first()
                    settingsRepository.updateUserSettings(currentSettings.copy(isNotificationsEnabled = event.enabled))
                }
                updateSuccess { it.copy(alertsEnabled = event.enabled) }
                if (!event.enabled) {
                    WeatherAlertScheduler.cancel(appContext)
                } else {
                    val state = _uiState.value as? AlertsUiState.Success ?: return
                    if (state.activeAlerts.isNotEmpty()) {
                        WeatherAlertScheduler.scheduleAlert(appContext)
                    }
                }
            }

            is AlertsUiEvent.OnRemoveAlert -> {
                viewModelScope.launch {
                    deleteAlert(event.id.toLongOrNull() ?: return@launch)
                }
                // If no alerts remain after removal, cancel the scheduled work
                val state = _uiState.value as? AlertsUiState.Success ?: return
                if (state.activeAlerts.size <= 1) {
                    WeatherAlertScheduler.cancel(appContext)
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

    private fun durationToMillis(duration: AlertDuration): Long = when (duration) {
        AlertDuration.OneHour -> 1 * 60 * 60 * 1000L
        AlertDuration.SixHours -> 6 * 60 * 60 * 1000L
        AlertDuration.TwentyFourHours -> 24 * 60 * 60 * 1000L
    }

    private fun AlertCase.toTitle(): String = when (this) {
        AlertCase.Storm -> "Storm warning"
        AlertCase.HeatAdvisory -> "Heat advisory"
        AlertCase.FlashFloodWatch -> "Flash flood watch"
        AlertCase.CustomTemperature -> "Custom temperature"
    }

    private fun com.wahid.wurly.domain.model.weather.WeatherAlert.toDescription(): String = when (this.alertCase) {
        AlertCase.Storm -> "Storm or thunder conditions detected"
        AlertCase.HeatAdvisory -> "High temperature risk"
        AlertCase.FlashFloodWatch -> "Heavy rain or flood risk"
        AlertCase.CustomTemperature -> "Trigger above: ${this.targetTemperature ?: 0}°"
    }

    private fun AlertCase.toIcon() = when (this) {
        AlertCase.Storm -> Icons.Filled.Warning
        AlertCase.HeatAdvisory -> Icons.Filled.Thermostat
        AlertCase.FlashFloodWatch -> Icons.Filled.NotificationsActive
        AlertCase.CustomTemperature -> Icons.Filled.Thermostat
    }

    private fun formatTimestamp(millis: Long): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).format(formatter)
    }
}