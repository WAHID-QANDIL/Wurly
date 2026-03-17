package com.wahid.wurly.presentation.screen.settings.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

/**
 * A section title label (e.g., "LOCATION", "UNITS", "LANGUAGE").
 *
 * Renders uppercase text in a semi-transparent white color at the top of
 * a settings card.
 */
@Composable
fun SettingsSectionTitle(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        text = title,
        modifier = modifier,
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
        ),
        color = Color.White.copy(alpha = 0.7f),
    )
}