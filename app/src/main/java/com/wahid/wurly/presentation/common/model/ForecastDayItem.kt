package com.wahid.wurly.presentation.common.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a single day in the 7-day forecast list.
 */
data class ForecastDayItem(
    val dayName: String,
    val condition: String,
    val conditionIcon: String,
    val highTemp: String,
    val lowTemp: String,
    val date: String,
    val time: String,
)