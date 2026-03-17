package com.wahid.wurly.presentation.common.model


/**
 * Represents a compact forecast entry (e.g., hourly) for display in a horizontal card row.
 */
data class ForecastCardItem(
    val timeLabel: String,
    val description: String,
    val temperature: String,
    val subtitle: String,
    val icon: String
)