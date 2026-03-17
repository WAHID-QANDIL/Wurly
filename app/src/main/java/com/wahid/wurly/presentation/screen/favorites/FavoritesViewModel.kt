package com.wahid.wurly.presentation.screen.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahid.wurly.R
import com.wahid.wurly.domain.usecase.GetFavoriteCityWeathers
import com.wahid.wurly.domain.usecase.RemoveCityFromFavorites
import com.wahid.wurly.presentation.common.model.FavoriteLocationItem
import com.wahid.wurly.utils.ResourceAccessor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteCityWeathers: GetFavoriteCityWeathers,
    private val removeCityFromFavorites: RemoveCityFromFavorites,
    private val resourceAccessor: ResourceAccessor
) : ViewModel() {

    init {
        viewModelScope.launch {
            getFavoriteCityWeathers().collectLatest { favorites ->
                favoritesCache.clear()
                val items = favorites.map { cityWeather ->
                    favoritesCache[cityWeather.city.id.toString()] = cityWeather.city
                    FavoriteLocationItem(
                        id = cityWeather.city.id.toString(),
                        cityName = "${cityWeather.city.name}, ${cityWeather.city.country}",
                        temperature = "${cityWeather.temperature.toInt()}${resourceAccessor.getString(R.string.weather_degree_symbol)}",
                        condition = cityWeather.condition,
                        conditionIcon = cityWeather.icon,
                        date = formatDate(cityWeather.dayTime),
                        time = formatTime(cityWeather.dayTime),
                    )
                }
                _uiState.update { current ->
                    if (current is FavoritesUiState.Success) current.copy(favorites = items) else current
                }
            }
        }
    }
    private val _uiState = MutableStateFlow<FavoritesUiState>(
        FavoritesUiState.Success(
            favorites = emptyList(),
            currentBackground = R.drawable.image,
        )
    )
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    fun onEvent(event: FavoritesUiEvent) {
        when (event) {
            is FavoritesUiEvent.OnRemoveFavorite -> {
                updateSuccess { state ->
                    state.copy(favorites = state.favorites.filter { it.id != event.id })
                }
                viewModelScope.launch {
                    favoritesCache[event.id]?.let { removeCityFromFavorites(it) }
                }
            }
        }
    }

    private inline fun updateSuccess(
        transform: (FavoritesUiState.Success) -> FavoritesUiState.Success,
    ) {
        _uiState.update { current ->
            if (current is FavoritesUiState.Success) transform(current) else current
        }
    }

    private val favoritesCache = mutableMapOf<String, com.wahid.wurly.domain.model.weather.City>()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    private fun formatDate(epochSeconds: Long): String =
        Instant.ofEpochSecond(epochSeconds).atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)

    private fun formatTime(epochSeconds: Long): String =
        Instant.ofEpochSecond(epochSeconds).atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter)
}