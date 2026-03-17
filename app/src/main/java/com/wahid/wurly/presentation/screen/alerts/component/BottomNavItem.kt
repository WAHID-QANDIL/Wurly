package com.wahid.wurly.presentation.screen.alerts.component

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a bottom navigation destination.
 */
data class NavItem(
    val icon: ImageVector,
    @param:StringRes val labelRes: Int,
    @param:StringRes val contentDescriptionRes: Int,
    val isSelected: Boolean = false,
)