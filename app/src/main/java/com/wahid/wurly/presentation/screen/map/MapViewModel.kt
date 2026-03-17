package com.wahid.wurly.presentation.screen.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.common.MapboxOptions
import com.mapbox.search.autocomplete.PlaceAutocomplete
import com.wahid.wurly.R
import com.wahid.wurly.domain.usecase.AddCityToFavorites
import com.wahid.wurly.domain.usecase.GetDayForecast
import com.wahid.wurly.domain.usecase.GetUserSettings
import com.wahid.wurly.presentation.common.model.LocationSuggestion
import com.wahid.wurly.utils.ResourceAccessor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val resourceAccessor: ResourceAccessor,
    private val addCityToFavorites: AddCityToFavorites,
    private val getDayForecast: GetDayForecast,
    private val getUserSettings: GetUserSettings,
) : ViewModel() {
    var placeAutocomplete: PlaceAutocomplete
    private val searchQuery = MutableStateFlow("")

    init {
        MapboxOptions.accessToken = resourceAccessor.getString(R.string.mapbox_access_token)
        placeAutocomplete = PlaceAutocomplete.create(locationProvider = null)
        observeSearchQueryChanges()
    }

    private val _uiState = MutableStateFlow<MapUiState>(
        MapUiState.Success(
            searchQuery = "",
            suggestions = emptyList(),
            selectedSuggestion = null,
            isSaving = false,
            currentBackground = R.drawable.image1,
        )
    )
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    fun onEvent(event: MapUiEvent) {
        when (event) {
            is MapUiEvent.OnSearchQueryChange -> {
                searchQuery.value = event.query
                updateSuccess { state ->
                    state.copy(
                        searchQuery = event.query,
                    )
                }
            }

            is MapUiEvent.OnClearSearch -> {
                searchQuery.value = ""
                updateSuccess { state ->
                    state.copy(
                        searchQuery = "",
                        suggestions = emptyList(),
                        selectedSuggestion = null,
                    )
                }
            }

            is MapUiEvent.OnSuggestionClick -> {
                updateSuccess { state ->
                    state.copy(
                        selectedSuggestion = event.suggestion,
                        searchQuery = event.suggestion.displayName,
                        suggestions = emptyList(),
                    )
                }
            }

            is MapUiEvent.OnMapTap -> {
                val suggestion = LocationSuggestion(
                    id = "pin_${event.latitude}_${event.longitude}",
                    displayName = event.label,
                    latitude = event.latitude,
                    longitude = event.longitude,
                )
                updateSuccess { state ->
                    state.copy(
                        selectedSuggestion = suggestion,
                        searchQuery = event.label,
                        suggestions = emptyList(),
                    )
                }
            }

            is MapUiEvent.OnSaveLocation -> {
                val selected = (_uiState.value as? MapUiState.Success)?.selectedSuggestion
                if (selected == null) {
                    _uiState.update { MapUiState.Error("No location selected") }
                    return
                }
                updateSuccess { state -> state.copy(isSaving = true) }
                viewModelScope.launch {
                    runCatching {
                        val settings = getUserSettings().first()
                        val params = mutableMapOf<String, String>().apply {
                            this["lat"] = selected.latitude.toString()
                            this["lon"] = selected.longitude.toString()
                            this["cnt"] = "${5 * 7}"
                            this["units"] = settings.weatherUnit.unit
                            this["lang"] = settings.language.lowercase()
                        }

                        val forecast = getDayForecast(params, isFavorite = true).first()
                        addCityToFavorites(forecast.city)
                    }.onSuccess {
                        updateSuccess { state -> state.copy(isSaving = false) }
                    }.onFailure { throwable ->
                        _uiState.update { MapUiState.Error(throwable.message ?: "Failed to save location") }
                    }
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQueryChanges() {
        viewModelScope.launch {
            searchQuery
                .debounce(1000)
                .collectLatest { query ->
                    if (query.isBlank()) {
                        updateSuccess { state -> state.copy(suggestions = emptyList()) }
                        return@collectLatest
                    }

                    val response = placeAutocomplete.suggestions(query = query)
                    if (!response.isValue) {
                        Log.e("SearchExample", "Error fetching suggestions: ${response.error}")
                        return@collectLatest
                    }

                    val suggestions = response.value.orEmpty()
                    if (suggestions.isEmpty()) {
                        updateSuccess { state -> state.copy(suggestions = emptyList()) }
                        return@collectLatest
                    }

                    val result = placeAutocomplete.select(suggestions.first())

                    _uiState.update {
                        if (result.isValue) {
                            val selected = result.value!!
                            MapUiState.Success(
                                searchQuery = query,
                                suggestions = suggestions.distinct().map { suggestion ->
                                    placeAutocomplete.select(suggestion).value?.let { selectedSuggestion ->
                                        Log.i("SearchExample", "Selected: $selectedSuggestion")
                                        LocationSuggestion(
                                            id = selectedSuggestion.id,
                                            displayName = selectedSuggestion.name,
                                            latitude = selectedSuggestion.coordinate.latitude(),
                                            longitude = selectedSuggestion.coordinate.longitude(),
                                        )
                                    } ?: LocationSuggestion(
                                        id = "0",
                                        displayName = suggestion.name,
                                        latitude = 0.0,
                                        longitude = 0.0,
                                    )
                                },
                                selectedSuggestion = LocationSuggestion(
                                    id = selected.id,
                                    displayName = selected.name,
                                    latitude = selected.coordinate.latitude(),
                                    longitude = selected.coordinate.longitude(),
                                ),
                                isSaving = false,
                                currentBackground = R.drawable.image1,
                            )
                        } else {
                            MapUiState.Error("Error selecting suggestion: ${result.error}")
                        }
                    }
                }
        }
    }

    private inline fun updateSuccess(
        transform: (MapUiState.Success) -> MapUiState.Success,
    ) {
        _uiState.update { current ->
            if (current is MapUiState.Success) transform(current) else current
        }
    }
}