package com.wahid.wurly.presentation.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mrtdk.glass.GlassBox
import com.mrtdk.glass.GlassContainer
import com.wahid.wurly.R
import com.wahid.wurly.presentation.common.rememberCachedBackgroundPainter
import com.wahid.wurly.presentation.screen.forecast.component.ForecastCardRow
import com.wahid.wurly.presentation.screen.home.component.TemperatureDisplay
import com.wahid.wurly.presentation.screen.home.component.WeatherDetailGrid
import com.wahid.wurly.presentation.screen.home.component.WeatherHeader

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState = HomeUiState.Loading,
    onEvent: (HomeUiEvent) -> Unit,
    onNavigateToForecast: () -> Unit
) {
    when (uiState) {
        is HomeUiState.Loading -> {
            LoadingState(modifier = modifier)
        }

        is HomeUiState.Error -> {
            ErrorState(
                modifier = modifier,
                message = uiState.message
            )
        }

        is HomeUiState.Success -> {
            HomeContent(
                modifier = modifier,
                state = uiState,
                onEvent = onEvent,
                onNavigateToForecast = onNavigateToForecast
            )
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun ErrorState(
    modifier: Modifier = Modifier,
    message: String,
) {
    GlassContainer(
        modifier = modifier.fillMaxSize(),
        content = {
            Image(
                painter = rememberCachedBackgroundPainter(R.drawable.image),
                contentDescription = stringResource(R.string.weather_background_cd),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    ) {
        GlassBox(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp),
            contentAlignment = Alignment.Center,
            scale = 0.7f,
            darkness = 0.4f
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    state: HomeUiState.Success,
    onEvent: (HomeUiEvent) -> Unit,
    onNavigateToForecast:()-> Unit
) {
    val horizontalPadding = dimensionResource(R.dimen.weather_screen_horizontal_padding)
    val topPadding = dimensionResource(R.dimen.weather_screen_top_padding)
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { onEvent(HomeUiEvent.OnRefresh) }
    )

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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding)
                    .padding(top = topPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                WeatherHeader(
                    modifier = Modifier.fillMaxWidth(),
                    locationName = state.locationName,
                    dateTime = state.dateTime,
                    onNavigateToForecast = onNavigateToForecast,
                )

                Spacer(
                    modifier = Modifier.height(
                        dimensionResource(R.dimen.weather_temperature_top_spacing)
                    )
                )

                TemperatureDisplay(
                    temperature = state.temperature,
                    condition = state.condition,
                )

                Spacer(
                    modifier = Modifier.height(
                        dimensionResource(R.dimen.weather_detail_grid_top_spacing)
                    )
                )

                with(glassScope) {
                    WeatherDetailGrid(
                        details = state.details,
                    )
                }

                Spacer(
                    modifier = Modifier.height(
                        dimensionResource(R.dimen.weather_detail_grid_top_spacing)
                    )
                )

                with(glassScope) {
                    ForecastCardRow(
                        items = state.forecastCards,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Spacer(
                    modifier = Modifier.height(
                        dimensionResource(R.dimen.weather_nav_height)
                    )
                )
            }

            PullRefreshIndicator(
                refreshing = state.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}