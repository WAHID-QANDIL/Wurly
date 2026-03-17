package com.wahid.wurly.presentation.screen.map

import androidx.annotation.DrawableRes
import com.wahid.wurly.presentation.common.model.LocationSuggestion

/**
 * Represents the complete UI state for the Map screen.
 */
sealed interface MapUiState {
    data object Loading : MapUiState

    data class Success(
        val searchQuery: String,
        val suggestions: List<LocationSuggestion>,
        val selectedSuggestion: LocationSuggestion?,
        val isSaving: Boolean,
        @param:DrawableRes val currentBackground: Int,
    ) : MapUiState

    data class Error(val message: String) : MapUiState
}

/**
 * One-shot events emitted from the Map screen UI.
 */
sealed interface MapUiEvent {
    data class OnSearchQueryChange(val query: String) : MapUiEvent
    data object OnClearSearch : MapUiEvent
    data class OnSuggestionClick(val suggestion: LocationSuggestion) : MapUiEvent
    data object OnSaveLocation : MapUiEvent
    data class OnMapTap(val latitude: Double, val longitude: Double, val label: String = "Pinned location") : MapUiEvent
}