package com.wahid.wurly.presentation.common.model

/**
 * Represents a location search suggestion in the Add Favorite screen.
 */
data class LocationSuggestion(
    val id: String,
    val displayName: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)