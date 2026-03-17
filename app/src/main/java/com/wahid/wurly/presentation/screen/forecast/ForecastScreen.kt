package com.wahid.wurly.presentation.screen.forecast

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mrtdk.glass.GlassContainer
import com.wahid.wurly.R
import com.wahid.wurly.presentation.common.model.ForecastDayItem
import com.wahid.wurly.presentation.common.rememberCachedBackgroundPainter
import com.wahid.wurly.presentation.screen.forecast.component.ForecastDayRow
import com.wahid.wurly.presentation.screen.forecast.component.ForecastTopBar
import com.wahid.wurly.ui.theme.WurlyTheme

// ─────────────────────────────────────────────────────────────
// Screen-level composable (interacts with ViewModel)
// ─────────────────────────────────────────────────────────────

@Composable
fun ForecastScreen(
    modifier: Modifier = Modifier,
    uiState: ForecastUiState = ForecastUiState.Loading,
    onEvent: () -> Unit,
    onBackClick: () -> Unit,

) {
    when (uiState) {
        is ForecastUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is ForecastUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        is ForecastUiState.Success -> {
            ForecastContent(
                modifier = modifier,
                state = uiState,
                onEvent = onEvent,
                onBackClick = onBackClick
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Stateless UI content composable
// ─────────────────────────────────────────────────────────────

/**
 * Stateless composable that renders the 7-day forecast UI.
 *
 * Uses a single [LazyColumn] to avoid nested-scrollable measuring exceptions.
 * The top bar is emitted as a sticky item, and each forecast row is a separate
 * item so only visible rows are composed.
 */
@Composable
private fun ForecastContent(
    modifier: Modifier = Modifier,
    state: ForecastUiState.Success,
    onEvent: () -> Unit,
    onBackClick: () -> Unit,
) {
    val horizontalPadding = dimensionResource(R.dimen.weather_screen_horizontal_padding)
    val topPadding = dimensionResource(R.dimen.weather_screen_top_padding)
    val rowSpacing = dimensionResource(R.dimen.forecast_row_spacing)
    val listTopSpacing = dimensionResource(R.dimen.forecast_list_top_spacing)
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
            verticalArrangement = Arrangement.spacedBy(rowSpacing),
        ) {
            // ── Top Bar ──
            item(key = "top_bar") {
                ForecastTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    onBackClick = onBackClick,
                    onOverflowClick = { },
                )
                Spacer(modifier = Modifier.height(listTopSpacing))
            }

            items(
                items = state.forecasts,
            ) { dayItem ->
                with(glassScope) {
                    ForecastDayRow(
                        dayName = dayItem.dayName,
                        condition = dayItem.condition,
                        conditionIcon = dayItem.conditionIcon,
                        highTemp = dayItem.highTemp,
                        lowTemp = dayItem.lowTemp,
                        date = dayItem.date,
                        time = dayItem.time,
                    )
                }
            }

            item(key = "bottom_spacer") {
                Spacer(modifier = Modifier.height(navHeight))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────

/*private fun previewForecasts() = listOf(
    ForecastDayItem("Monday", "Sunny", Icons.Outlined.WbSunny, "25", "14"),
    ForecastDayItem("Tuesday", "Partly Cloudy", Icons.Outlined.Cloud, "22", "12"),
    ForecastDayItem("Wednesday", "Showers", Icons.Filled.WaterDrop, "18", "10"),
    ForecastDayItem("Thursday", "Cloudy", Icons.Filled.Cloud, "20", "11"),
    ForecastDayItem("Friday", "Stormy", Icons.Filled.Thunderstorm, "17", "9"),
    ForecastDayItem("Saturday", "Few Clouds", Icons.Outlined.Cloud, "21", "13"),
    ForecastDayItem("Sunday", "Clear Sky", Icons.Outlined.WbSunny, "26", "15"),
)*/

@Preview(
    name = "Forecast – Light",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun ForecastScreenLightPreview() {
    WurlyTheme {
        ForecastScreen(
            uiState = ForecastUiState.Success(
                forecasts = emptyList(),
                currentBackground = R.drawable.image,
            ),
            onEvent = {},
                onBackClick = {}
        )
    }
}

@Preview(
    name = "Forecast – Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun ForecastScreenDarkPreview() {
    WurlyTheme {
        ForecastScreen(
            uiState = ForecastUiState.Success(
                forecasts = emptyList(),
                currentBackground = R.drawable.image,
            ),
            onEvent = {},
            onBackClick = {}
        )
    }
}