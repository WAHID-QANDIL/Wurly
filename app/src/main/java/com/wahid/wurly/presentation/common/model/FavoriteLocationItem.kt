package com.wahid.wurly.presentation.common.model

/**
 * Represents a saved favorite location displayed in the Favorites list.
 */
data class FavoriteLocationItem(
    val id: String,
    val cityName: String,
    val temperature: String,
    val condition: String,
    val conditionIcon: String,
    val date: String,
    val time: String,
)