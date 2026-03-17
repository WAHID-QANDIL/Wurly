package com.wahid.wurly.presentation.screen.forecast

import androidx.annotation.DrawableRes
import com.wahid.wurly.presentation.common.model.ForecastDayItem

/**
 * Represents the complete UI state for the 7-Day Forecast screen.
 */
sealed interface ForecastUiState {
    data object Loading : ForecastUiState

    data class Success(
        val forecasts: List<ForecastDayItem>,
        @param:DrawableRes val currentBackground: Int,
    ) : ForecastUiState

    data class Error(val message: String) : ForecastUiState
}

/**
 * One-shot events emitted from the 7-Day Forecast screen UI.
 */
sealed interface ForecastUiEvent {
    data object OnBackClick : ForecastUiEvent
    data object OnOverflowClick : ForecastUiEvent
    data class OnNavItemClick(val index: Int) : ForecastUiEvent
}