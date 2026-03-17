package com.wahid.wurly.presentation.screen.favorites

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.wahid.wurly.presentation.navigation.DestinationRoutes

fun NavGraphBuilder.favorites(
    onFavoriteClick: (Long) -> Unit,
) {
    composable<DestinationRoutes.Favorites> {
        val viewModel: FavoritesViewModel = hiltViewModel()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        FavoritesScreen(
            uiState = uiState.value,
            onFavoriteClick = onFavoriteClick,
            onRemoveFavorite = { id -> viewModel.onEvent(FavoritesUiEvent.OnRemoveFavorite(id)) },
        )
    }
}