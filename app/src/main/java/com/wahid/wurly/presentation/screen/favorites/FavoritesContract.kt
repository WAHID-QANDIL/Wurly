package com.wahid.wurly.presentation.screen.favorites

import androidx.annotation.DrawableRes
import com.wahid.wurly.presentation.common.model.FavoriteLocationItem

/**
 * Represents the complete UI state for the Favorites screen.
 */
sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState

    data class Success(
        val favorites: List<FavoriteLocationItem>,
        @param:DrawableRes val currentBackground: Int,
    ) : FavoritesUiState

    data class Error(val message: String) : FavoritesUiState
}

/**
 * One-shot events emitted from the Favorites screen UI.
 */
sealed interface FavoritesUiEvent {
    data object OnBackClick : FavoritesUiEvent
    data object OnSearchClick : FavoritesUiEvent
    data object OnAddFavoriteClick : FavoritesUiEvent
    data class OnRemoveFavorite(val id: String) : FavoritesUiEvent
    data class OnFavoriteClick(val id: String) : FavoritesUiEvent
    data class OnNavItemClick(val index: Int) : FavoritesUiEvent
}