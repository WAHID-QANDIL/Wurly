package com.wahid.wurly.presentation.screen.settings

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mrtdk.glass.GlassBox
import com.mrtdk.glass.GlassBoxScope
import com.mrtdk.glass.GlassContainer
import com.wahid.wurly.R
import com.wahid.wurly.presentation.common.rememberCachedBackgroundPainter
import com.wahid.wurly.presentation.screen.settings.component.SegmentedOption
import com.wahid.wurly.presentation.screen.settings.component.SettingsLanguageRow
import com.wahid.wurly.presentation.screen.settings.component.SettingsNavigateRow
import com.wahid.wurly.presentation.screen.settings.component.SettingsSectionTitle
import com.wahid.wurly.presentation.screen.settings.component.SettingsSegmentedButtons
import com.wahid.wurly.presentation.screen.settings.component.SettingsToggleRow
import com.wahid.wurly.presentation.screen.settings.component.SettingsTopBar
import com.wahid.wurly.ui.theme.WurlyTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState = SettingsUiState.Loading,
    onEvent: (SettingsUiEvent) -> Unit,
) {
    when (uiState) {
        is SettingsUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is SettingsUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        is SettingsUiState.Success -> {
            SettingsContent(
                modifier = modifier,
                state = uiState,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier,
    state: SettingsUiState.Success,
    onEvent: (SettingsUiEvent) -> Unit,
) {
    val horizontalPadding = dimensionResource(R.dimen.weather_screen_horizontal_padding)
    val topPadding = dimensionResource(R.dimen.weather_screen_top_padding)
    val sectionSpacing = dimensionResource(R.dimen.settings_section_spacing)
    val listTopSpacing = dimensionResource(R.dimen.settings_list_top_spacing)
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
                    onBackClick = {  },
                )
                Spacer(modifier = Modifier.height(listTopSpacing))
            }

            item(key = "location_section") {
                with(glassScope) {
                    LocationSection(
                        useGps = state.useGps,
                        onToggleGps = { onEvent(SettingsUiEvent.OnToggleGps(it)) },
                        onSelectFromMap = {  },
                    )
                }
            }

            item(key = "units_section") {
                with(glassScope) {
                    UnitsSection(
                        selectedTemperatureUnit = state.selectedTemperatureUnit,
                        selectedWindSpeedUnit = state.selectedWindSpeedUnit,
                        onTemperatureUnitChange = {
                            onEvent(SettingsUiEvent.OnTemperatureUnitChange(it))
                        },
                        onWindSpeedUnitChange = {
                            onEvent(SettingsUiEvent.OnWindSpeedUnitChange(it))
                        },
                    )
                }
            }

            item(key = "language_section") {
                with(glassScope) {
                    LanguageSection(
                        selectedLanguage = state.selectedLanguage,
                        currentLanguageDisplayName = state.currentLanguageDisplayName,
                        onLanguageChange = {
                            onEvent(SettingsUiEvent.OnLanguageChange(it))
                        },
                    )
                }
            }

            item(key = "bottom_spacer") {
                Spacer(modifier = Modifier.height(navHeight))
            }
        }
    }
}

@Composable
private fun GlassBoxScope.LocationSection(
    modifier: Modifier = Modifier,
    useGps: Boolean,
    onToggleGps: (Boolean) -> Unit,
    onSelectFromMap: () -> Unit,
) {
    val cardCornerRadius = dimensionResource(R.dimen.settings_card_corner_radius)
    val cardPadding = dimensionResource(R.dimen.settings_card_padding)
    val sectionTitleSpacing = dimensionResource(R.dimen.settings_section_title_bottom_spacing)
    val rowVerticalPadding = dimensionResource(R.dimen.settings_row_vertical_padding)

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
            SettingsSectionTitle(
                title = stringResource(R.string.settings_section_location),
            )

            Spacer(modifier = Modifier.height(sectionTitleSpacing))

            SettingsToggleRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = rowVerticalPadding),
                icon = Icons.Filled.NearMe,
                iconContentDescription = stringResource(R.string.settings_gps_icon_cd),
                label = stringResource(R.string.settings_use_gps),
                checked = useGps,
                onCheckedChange = onToggleGps,
            )

            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

            SettingsNavigateRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = rowVerticalPadding),
                icon = Icons.Filled.Map,
                iconContentDescription = stringResource(R.string.settings_map_icon_cd),
                label = stringResource(R.string.settings_select_from_map),
                onClick = onSelectFromMap,
            )
        }
    }
}

@Composable
private fun GlassBoxScope.UnitsSection(
    modifier: Modifier = Modifier,
    selectedTemperatureUnit: TemperatureUnit,
    selectedWindSpeedUnit: WindSpeedUnit,
    onTemperatureUnitChange: (TemperatureUnit) -> Unit,
    onWindSpeedUnitChange: (WindSpeedUnit) -> Unit,
) {
    val cardCornerRadius = dimensionResource(R.dimen.settings_card_corner_radius)
    val cardPadding = dimensionResource(R.dimen.settings_card_padding)
    val sectionTitleSpacing = dimensionResource(R.dimen.settings_section_title_bottom_spacing)
    val labelBottomSpacing = dimensionResource(R.dimen.settings_label_bottom_spacing)

    val temperatureOptions = listOf(
        SegmentedOption(TemperatureUnit.Celsius, stringResource(R.string.settings_unit_celsius)),
        SegmentedOption(
            TemperatureUnit.Fahrenheit,
            stringResource(R.string.settings_unit_fahrenheit)
        ),
        SegmentedOption(TemperatureUnit.Kelvin, stringResource(R.string.settings_unit_kelvin)),
    )

    val windSpeedOptions = listOf(
        SegmentedOption(WindSpeedUnit.Ms, stringResource(R.string.settings_unit_ms)),
        SegmentedOption(WindSpeedUnit.Mph, stringResource(R.string.settings_unit_mph)),
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
            SettingsSectionTitle(
                title = stringResource(R.string.settings_section_units),
            )

            Spacer(modifier = Modifier.height(sectionTitleSpacing))

            Text(
                text = stringResource(R.string.settings_temperature),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
            )

            Spacer(modifier = Modifier.height(labelBottomSpacing))

            SettingsSegmentedButtons(
                options = temperatureOptions,
                selectedValue = selectedTemperatureUnit,
                onOptionSelected = onTemperatureUnitChange,
            )

            Spacer(modifier = Modifier.height(sectionTitleSpacing))


            Text(
                text = stringResource(R.string.settings_wind_speed),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
            )

            Spacer(modifier = Modifier.height(labelBottomSpacing))

            SettingsSegmentedButtons(
                options = windSpeedOptions,
                selectedValue = selectedWindSpeedUnit,
                onOptionSelected = onWindSpeedUnitChange,
            )
        }
    }
}

@Composable
private fun GlassBoxScope.LanguageSection(
    modifier: Modifier = Modifier,
    selectedLanguage: AppLanguage,
    currentLanguageDisplayName: String,
    onLanguageChange: (AppLanguage) -> Unit,
) {
    val cardCornerRadius = dimensionResource(R.dimen.settings_card_corner_radius)
    val cardPadding = dimensionResource(R.dimen.settings_card_padding)
    val sectionTitleSpacing = dimensionResource(R.dimen.settings_section_title_bottom_spacing)

    val languageOptions = listOf(
        SegmentedOption(AppLanguage.EN, stringResource(R.string.settings_lang_en)),
        SegmentedOption(AppLanguage.AR, stringResource(R.string.settings_lang_ar)),
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
            SettingsSectionTitle(
                title = stringResource(R.string.settings_section_language),
            )

            Spacer(modifier = Modifier.height(sectionTitleSpacing))

            SettingsLanguageRow(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Filled.Language,
                iconContentDescription = stringResource(R.string.settings_language_icon_cd),
                label = stringResource(R.string.settings_app_language),
                subtitle = stringResource(
                    R.string.settings_currently_language,
                    currentLanguageDisplayName,
                ),
                trailing = {
                    SettingsSegmentedButtons(
                        options = languageOptions,
                        selectedValue = selectedLanguage,
                        onOptionSelected = onLanguageChange,
                    )
                },
            )
        }
    }
}

private fun previewState() = SettingsUiState.Success(
    useGps = true,
    selectedTemperatureUnit = TemperatureUnit.Celsius,
    selectedWindSpeedUnit = WindSpeedUnit.Ms,
    selectedLanguage = AppLanguage.EN,
    currentLanguageDisplayName = "English",
    currentBackground = R.drawable.image1,
)

@Preview(
    name = "Settings – Light",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun SettingsScreenLightPreview() {
    WurlyTheme {
        SettingsScreen(
            uiState = previewState(),
            onEvent = {},
        )
    }
}

@Preview(
    name = "Settings – Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun SettingsScreenDarkPreview() {
    WurlyTheme {
        SettingsScreen(
            uiState = previewState(),
            onEvent = {},
        )
    }
}