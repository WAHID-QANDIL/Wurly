package com.wahid.wurly.presentation.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.mrtdk.glass.GlassContainer
import com.wahid.wurly.R
import com.wahid.wurly.presentation.common.rememberCachedBackgroundPainter
import com.wahid.wurly.presentation.common.components.TemperatureDisplay
import com.wahid.wurly.presentation.common.components.WeatherDetailGrid
import com.wahid.wurly.presentation.common.components.WeatherHeader


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState = HomeUiState.Loading,
    onEvent: (HomeUiEvent) -> Unit,
) {
    when (uiState) {
        is HomeUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is HomeUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        is HomeUiState.Success -> {
            HomeContent(
                modifier = modifier,
                state = uiState,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    state: HomeUiState.Success,
    onEvent: (HomeUiEvent) -> Unit,
) {
    val horizontalPadding = dimensionResource(R.dimen.weather_screen_horizontal_padding)
    val topPadding = dimensionResource(R.dimen.weather_screen_top_padding)

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
        // Capture the GlassBoxScope so we can forward it into the grid
        val glassScope = this

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding)
                    .padding(top = topPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // ── Header ──
                WeatherHeader(
                    modifier = Modifier.fillMaxWidth(),
                    locationName = state.locationName,
                    dateTime = state.dayWeather.dayTimeString,
                    onBackClick = { onEvent(HomeUiEvent.OnBackClick) },
                )

                Spacer(
                    modifier = Modifier.height(
                        dimensionResource(R.dimen.weather_temperature_top_spacing)
                    )
                )

                // ── Temperature ──
                TemperatureDisplay(
                    temperature = "${state.dayWeather.temp}",
                    condition = state.condition,
                )

                Spacer(
                    modifier = Modifier.height(
                        dimensionResource(R.dimen.weather_detail_grid_top_spacing)
                    )
                )

                // ── Detail Grid (glass cards) ──
                // Explicit receiver needed because Column breaks the implicit GlassBoxScope chain
                with(glassScope) {
                    WeatherDetailGrid(
                        details = state.details,
                    )
                }

                // Bottom spacer so content isn't hidden behind the nav bar
                Spacer(
                    modifier = Modifier.height(
                        dimensionResource(R.dimen.weather_nav_height)
                    )
                )
            }


        }
    }
}