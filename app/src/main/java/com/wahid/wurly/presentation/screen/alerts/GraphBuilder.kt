package com.wahid.wurly.presentation.screen.alerts

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.wahid.wurly.presentation.navigation.DestinationRoutes

fun NavGraphBuilder.alerts() {
    composable<DestinationRoutes.Alerts> {
        val viewModel: AlertsViewModel = hiltViewModel()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        AlertsScreen(
            uiState = uiState.value,
            onEvent = viewModel::onEvent,
        )
    }
}