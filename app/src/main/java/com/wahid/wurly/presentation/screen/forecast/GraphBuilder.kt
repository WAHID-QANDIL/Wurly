package com.wahid.wurly.presentation.screen.forecast

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.wahid.wurly.presentation.navigation.DestinationRoutes

fun NavGraphBuilder.forecast() {
    composable<DestinationRoutes.Forecast> {
        val viewModel: ForecastViewModel = hiltViewModel()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        ForecastScreen(
            uiState = uiState.value,
            onEvent = viewModel::onEvent,
        )
    }
}