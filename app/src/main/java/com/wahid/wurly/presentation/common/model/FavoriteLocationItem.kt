package com.wahid.wurly.presentation.common.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a saved favorite location displayed in the Favorites list.
 */
data class FavoriteLocationItem(
    val id: String,
    val cityName: String,
    val temperature: String,
    val condition: String,
    val conditionIcon: ImageVector,
    @param:DrawableRes val previewImage: Int,
)