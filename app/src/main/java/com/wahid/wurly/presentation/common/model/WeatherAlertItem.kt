package com.wahid.wurly.presentation.common.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a single saved weather alert displayed in the active alerts list.
 */
data class WeatherAlertItem(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val timestamp: String,
)