package com.wahid.wurly.presentation.screen.home

import androidx.annotation.DrawableRes
import com.wahid.wurly.domain.model.weather.Weather
import com.wahid.wurly.presentation.common.model.ForecastCardItem
import com.wahid.wurly.presentation.screen.home.component.WeatherDetailItem

/**
 * Represents the complete UI state for the Weather screen.
 */
sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val weather: Weather,
        val locationName: String,
        val dateTime: String,
        val temperature: String,
        val condition: String,
        val details: List<WeatherDetailItem>,
        val forecastCards: List<ForecastCardItem>,
        val isRefreshing: Boolean = false,
        val locationEnabled: Boolean = false,
        @param:DrawableRes val currentBackground: Int
    ) : HomeUiState

    data class Error(val message: String) : HomeUiState
}

/**
 * One-shot events emitted from the Weather screen UI.
 */
sealed interface HomeUiEvent {
    data object OnRefresh : HomeUiEvent
}