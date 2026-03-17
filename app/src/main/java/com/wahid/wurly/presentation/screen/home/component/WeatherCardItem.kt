package com.wahid.wurly.presentation.screen.home.component

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector


/**
 * Represents a single weather detail metric (e.g., Precipitation, Wind, etc.)
 */
data class WeatherDetailItem(
    val icon: ImageVector,
    @param:StringRes val iconContentDescriptionRes: Int,
    @param:StringRes val labelRes: Int,
    val value: String,
    val subtitle: String,
)