package com.wahid.wurly.presentation.screen.alerts

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mrtdk.glass.GlassBox
import com.mrtdk.glass.GlassBoxScope
import com.mrtdk.glass.GlassContainer
import com.wahid.wurly.R
import com.wahid.wurly.presentation.common.rememberCachedBackgroundPainter
import com.wahid.wurly.presentation.common.model.WeatherAlertItem
import com.wahid.wurly.presentation.screen.alerts.component.AddAlertButton
import com.wahid.wurly.presentation.screen.alerts.component.AlertListItem
import com.wahid.wurly.presentation.screen.alerts.component.AlertsRadioRow
import com.wahid.wurly.presentation.screen.alerts.component.AlertsToggleRow
import com.wahid.wurly.presentation.screen.settings.component.SegmentedOption
import com.wahid.wurly.presentation.screen.settings.component.SettingsSectionTitle
import com.wahid.wurly.presentation.screen.settings.component.SettingsSegmentedButtons
import com.wahid.wurly.presentation.screen.settings.component.SettingsTopBar
import com.wahid.wurly.ui.theme.WurlyTheme

@Composable
fun AlertsScreen(
    modifier: Modifier = Modifier,
    uiState: AlertsUiState = AlertsUiState.Loading,
    onEvent: (AlertsUiEvent) -> Unit,
) {
    when (uiState) {
        is AlertsUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is AlertsUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        is AlertsUiState.Success -> {
            AlertsContent(
                modifier = modifier,
                state = uiState,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
private fun AlertsContent(
    modifier: Modifier = Modifier,
    state: AlertsUiState.Success,
    onEvent: (AlertsUiEvent) -> Unit,
) {
    val horizontalPadding = dimensionResource(R.dimen.weather_screen_horizontal_padding)
    val topPadding = dimensionResource(R.dimen.weather_screen_top_padding)
    val sectionSpacing = dimensionResource(R.dimen.alerts_section_spacing)
    val navHeight = dimensionResource(R.dimen.weather_nav_height)

    GlassContainer(
        modifier = modifier.fillMaxSize(),
        content = {
            Image(
                painter = rememberCachedBackgroundPainter(state.currentBackground),
                contentDescription = stringResource(R.string.weather_background_cd),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        },
    ) {
        val glassScope = this

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding)
                .padding(top = topPadding),
            verticalArrangement = Arrangement.spacedBy(sectionSpacing),
        ) {

            item(key = "top_bar") {
                SettingsTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.alerts_title),
                    onBackClick = { onEvent(AlertsUiEvent.OnBackClick) },
                )
            }


            item(key = "add_button") {
                AddAlertButton(
                    onClick = { onEvent(AlertsUiEvent.OnAddAlertClick) },
                )
            }

            item(key = "alerts_enabled") {
                with(glassScope) {
                    AlertsEnabledSection(
                        checked = state.alertsEnabled,
                        onCheckedChange = { onEvent(AlertsUiEvent.OnToggleAlerts(it)) },
                    )
                }
            }


            item(key = "active_duration") {
                with(glassScope) {
                    ActiveDurationSection(
                        selectedDuration = state.activeDuration,
                        onDurationChange = { onEvent(AlertsUiEvent.OnDurationChange(it)) },
                    )
                }
            }


            item(key = "notification_style") {
                with(glassScope) {
                    NotificationStyleSection(
                        selectedStyle = state.notificationStyle,
                        onStyleChange = { onEvent(AlertsUiEvent.OnNotificationStyleChange(it)) },
                    )
                }
            }


            if (state.activeAlerts.isEmpty()) {
                item(key = "empty_alerts") {
                    Spacer(modifier = Modifier.height(sectionSpacing))
                    Text(
                        text = stringResource(R.string.alerts_empty),
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                item(key = "alerts_section_title") {
                    Spacer(modifier = Modifier.height(sectionSpacing))
                    SettingsSectionTitle(
                        title = stringResource(R.string.alerts_section_active),
                    )
                }

                items(
                    items = state.activeAlerts,
                    key = { it.id },
                ) { alert ->
                    with(glassScope) {
                        AlertListItem(
                            icon = alert.icon,
                            title = alert.title,
                            description = alert.description,
                            timestamp = alert.timestamp,
                            onRemoveClick = { onEvent(AlertsUiEvent.OnRemoveAlert(alert.id)) },
                        )
                    }
                }
            }


            item(key = "bottom_spacer") {
                Spacer(modifier = Modifier.height(navHeight))
            }
        }
    }
}


@Composable
private fun GlassBoxScope.AlertsEnabledSection(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val cardCornerRadius = dimensionResource(R.dimen.alerts_card_corner_radius)
    val cardPadding = dimensionResource(R.dimen.alerts_card_padding)

    GlassBox(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(cardCornerRadius),
        scale = 0.6f,
        darkness = 0.35f,
    ) {
        AlertsToggleRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(cardPadding),
            title = stringResource(R.string.alerts_enabled),
            subtitle = stringResource(R.string.alerts_enabled_subtitle),
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
private fun GlassBoxScope.ActiveDurationSection(
    modifier: Modifier = Modifier,
    selectedDuration: AlertDuration,
    onDurationChange: (AlertDuration) -> Unit,
) {
    val cardCornerRadius = dimensionResource(R.dimen.alerts_card_corner_radius)
    val cardPadding = dimensionResource(R.dimen.alerts_card_padding)
    val iconSpacing = dimensionResource(R.dimen.alerts_row_icon_spacing)

    val durationOptions = listOf(
        SegmentedOption(AlertDuration.OneHour, stringResource(R.string.alerts_duration_1h)),
        SegmentedOption(AlertDuration.SixHours, stringResource(R.string.alerts_duration_6h)),
        SegmentedOption(
            AlertDuration.TwentyFourHours,
            stringResource(R.string.alerts_duration_24h)
        ),
    )

    GlassBox(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(cardCornerRadius),
        scale = 0.6f,
        darkness = 0.35f,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(cardPadding),
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.AccessTime,
                    contentDescription = stringResource(R.string.alerts_active_duration_icon_cd),
                    modifier = Modifier
                        .padding(end = iconSpacing)
                        .size(dimensionResource(R.dimen.alerts_row_icon_size)),
                    tint = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = stringResource(R.string.alerts_active_duration),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = Color.White,
                )
            }

            Spacer(
                modifier = Modifier.height(
                    dimensionResource(R.dimen.settings_section_title_bottom_spacing)
                )
            )

            SettingsSegmentedButtons(
                options = durationOptions,
                selectedValue = selectedDuration,
                onOptionSelected = onDurationChange,
            )
        }
    }
}

@Composable
private fun GlassBoxScope.NotificationStyleSection(
    modifier: Modifier = Modifier,
    selectedStyle: NotificationStyle,
    onStyleChange: (NotificationStyle) -> Unit,
) {
    val cardCornerRadius = dimensionResource(R.dimen.alerts_card_corner_radius)
    val cardPadding = dimensionResource(R.dimen.alerts_card_padding)
    val iconSpacing = dimensionResource(R.dimen.alerts_row_icon_spacing)

    GlassBox(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(cardCornerRadius),
        scale = 0.6f,
        darkness = 0.35f,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(cardPadding),
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = stringResource(R.string.alerts_notification_style_icon_cd),
                    modifier = Modifier
                        .padding(end = iconSpacing)
                        .size(dimensionResource(R.dimen.alerts_row_icon_size)),
                    tint = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = stringResource(R.string.alerts_notification_style),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = Color.White,
                )
            }

            Spacer(
                modifier = Modifier.height(
                    dimensionResource(R.dimen.settings_section_title_bottom_spacing)
                )
            )

            AlertsRadioRow(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Outlined.NotificationsNone,
                iconContentDescription = stringResource(R.string.alerts_style_standard_icon_cd),
                label = stringResource(R.string.alerts_style_standard),
                selected = selectedStyle == NotificationStyle.Standard,
                onClick = { onStyleChange(NotificationStyle.Standard) },
            )

            AlertsRadioRow(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Outlined.Alarm,
                iconContentDescription = stringResource(R.string.alerts_style_alarm_icon_cd),
                label = stringResource(R.string.alerts_style_alarm),
                selected = selectedStyle == NotificationStyle.Alarm,
                onClick = { onStyleChange(NotificationStyle.Alarm) },
            )
        }
    }
}


private fun previewAlerts() = listOf(
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

private fun previewState() = AlertsUiState.Success(
    alertsEnabled = true,
    activeDuration = AlertDuration.SixHours,
    notificationStyle = NotificationStyle.Standard,
    activeAlerts = previewAlerts(),
    currentBackground = R.drawable.image1,
)

@Preview(
    name = "Alerts – Light",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun AlertsScreenLightPreview() {
    WurlyTheme {
        AlertsScreen(
            uiState = previewState(),
            onEvent = {},
        )
    }
}

@Preview(
    name = "Alerts – Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun AlertsScreenDarkPreview() {
    WurlyTheme {
        AlertsScreen(
            uiState = previewState(),
            onEvent = {},
        )
    }
}

@Preview(
    name = "Alerts – Empty",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun AlertsScreenEmptyPreview() {
    WurlyTheme {
        AlertsScreen(
            uiState = AlertsUiState.Success(
                alertsEnabled = true,
                activeDuration = AlertDuration.SixHours,
                notificationStyle = NotificationStyle.Standard,
                activeAlerts = emptyList(),
                currentBackground = R.drawable.image1,
            ),
            onEvent = {},
        )
    }
}