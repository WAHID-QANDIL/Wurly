package com.wahid.wurly.presentation.screen.home

import androidx.annotation.DrawableRes
import com.wahid.wurly.domain.model.DayWeather
import com.wahid.wurly.presentation.common.components.WeatherDetailItem

/**
 * Represents the complete UI state for the Weather screen.
 */
sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val dayWeather: DayWeather,
        val locationName: String,
        val dateTime: String,
        val temperature: String,
        val condition: String,
        val details: List<WeatherDetailItem>,
        @param:DrawableRes val currentBackground: Int
    ) : HomeUiState

    data class Error(val message: String) : HomeUiState
}

/**
 * One-shot events emitted from the Weather screen UI.
 */
sealed interface HomeUiEvent {
    data object OnBackClick : HomeUiEvent
    data class OnNavItemClick(val index: Int) : HomeUiEvent
}