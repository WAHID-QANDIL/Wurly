package com.wahid.wurly.presentation.screen.addfavorite

import androidx.lifecycle.ViewModel
import com.wahid.wurly.R
import com.wahid.wurly.presentation.common.model.LocationSuggestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddFavoriteViewModel @Inject constructor() : ViewModel() {

    private val allCities = listOf(
        LocationSuggestion(id = "1", displayName = "San Francisco, CA", latitude = 37.7749, longitude = -122.4194),
        LocationSuggestion(id = "2", displayName = "San Francisco, Panama", latitude = 8.4312, longitude = -80.1133),
        LocationSuggestion(id = "3", displayName = "San Fernando, Spain", latitude = 36.4754, longitude = -6.1972),
        LocationSuggestion(id = "4", displayName = "Santiago, Chile", latitude = -33.4489, longitude = -70.6693),
        LocationSuggestion(id = "5", displayName = "Santa Cruz, Bolivia", latitude = -17.7863, longitude = -63.1812),
    )

    private val _uiState = MutableStateFlow<AddFavoriteUiState>(
        AddFavoriteUiState.Success(
            searchQuery = "",
            suggestions = emptyList(),
            selectedSuggestion = null,
            isSaving = false,
            currentBackground = R.drawable.image1,
        )
    )
    val uiState: StateFlow<AddFavoriteUiState> = _uiState.asStateFlow()

    fun onEvent(event: AddFavoriteUiEvent) {
        when (event) {
            is AddFavoriteUiEvent.OnBackClick -> { /* Handle navigation back */ }

            is AddFavoriteUiEvent.OnSearchQueryChange -> {
                updateSuccess { state ->
                    val filtered = if (event.query.isBlank()) {
                        emptyList()
                    } else {
                        allCities.filter {
                            it.displayName.contains(event.query, ignoreCase = true)
                        }
                    }
                    state.copy(
                        searchQuery = event.query,
                        suggestions = filtered,
                    )
                }
            }

            is AddFavoriteUiEvent.OnClearSearch -> {
                updateSuccess { state ->
                    state.copy(
                        searchQuery = "",
                        suggestions = emptyList(),
                        selectedSuggestion = null,
                    )
                }
            }

            is AddFavoriteUiEvent.OnSuggestionClick -> {
                updateSuccess { state ->
                    state.copy(
                        selectedSuggestion = event.suggestion,
                        searchQuery = event.suggestion.displayName,
                        suggestions = emptyList(),
                    )
                }
            }

            is AddFavoriteUiEvent.OnSaveLocation -> {
                // TODO: Persist the selected location and navigate back
                updateSuccess { state -> state.copy(isSaving = true) }
            }
        }
    }

    private inline fun updateSuccess(
        transform: (AddFavoriteUiState.Success) -> AddFavoriteUiState.Success,
    ) {
        _uiState.update { current ->
            if (current is AddFavoriteUiState.Success) transform(current) else current
        }
    }
}