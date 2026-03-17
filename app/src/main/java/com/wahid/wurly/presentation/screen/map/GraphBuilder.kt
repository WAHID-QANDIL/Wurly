package com.wahid.wurly.presentation.screen.map

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.wahid.wurly.presentation.navigation.DestinationRoutes

fun NavGraphBuilder.map() {
    composable<DestinationRoutes.Map> {
        val viewModel: MapViewModel = hiltViewModel()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        MapScreen(
            uiState = uiState.value,
            onEvent = viewModel::onEvent,
        )
    }
}