package com.wahid.wurly.presentation.screen.addfavorite

import androidx.annotation.DrawableRes
import com.wahid.wurly.presentation.common.model.LocationSuggestion

/**
 * Represents the complete UI state for the Add Favorite screen.
 */
sealed interface AddFavoriteUiState {
    data object Loading : AddFavoriteUiState

    data class Success(
        val searchQuery: String,
        val suggestions: List<LocationSuggestion>,
        val selectedSuggestion: LocationSuggestion?,
        val isSaving: Boolean,
        @param:DrawableRes val currentBackground: Int,
    ) : AddFavoriteUiState

    data class Error(val message: String) : AddFavoriteUiState
}

/**
 * One-shot events emitted from the Add Favorite screen UI.
 */
sealed interface AddFavoriteUiEvent {
    data object OnBackClick : AddFavoriteUiEvent
    data class OnSearchQueryChange(val query: String) : AddFavoriteUiEvent
    data object OnClearSearch : AddFavoriteUiEvent
    data class OnSuggestionClick(val suggestion: LocationSuggestion) : AddFavoriteUiEvent
    data object OnSaveLocation : AddFavoriteUiEvent
}