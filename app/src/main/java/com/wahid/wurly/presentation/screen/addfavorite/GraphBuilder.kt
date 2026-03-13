package com.wahid.wurly.presentation.screen.addfavorite

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.wahid.wurly.presentation.navigation.DestinationRoutes

fun NavGraphBuilder.addFavorite() {
    composable<DestinationRoutes.AddFavorite> {
        val viewModel: AddFavoriteViewModel = hiltViewModel()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        AddFavoriteScreen(
            uiState = uiState.value,
            onEvent = viewModel::onEvent,
        )
    }
}