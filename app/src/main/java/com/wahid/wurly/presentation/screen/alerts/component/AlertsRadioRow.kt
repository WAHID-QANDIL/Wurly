package com.wahid.wurly.presentation.screen.alerts.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.wahid.wurly.R

/**
 * A row with an icon, label, and a trailing radio button.
 *
 * Used for selecting a notification style (Standard / Alarm) and alert type.
 * Clicking anywhere in the row selects the option.
 *
 * @param isDark When true (default), renders text/icons in white suitable for glass/dark
 *               backgrounds. When false, uses the theme's onSurface colour for light
 *               backgrounds such as the Add Alert bottom-sheet.
 *
 * Stateless — the selected state and click callback are hoisted.
 */
@Composable
fun AlertsRadioRow(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconContentDescription: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    isDark: Boolean = true,
) {
    val contentColor = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
    val unselectedRadioColor = if (isDark) Color.White.copy(alpha = 0.5f)
    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)

    Row(
        modifier = modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconContentDescription,
            modifier = Modifier.size(dimensionResource(R.dimen.alerts_row_icon_size)),
            tint = contentColor,
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.alerts_row_icon_spacing)))

        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
            ),
            color = contentColor,
        )

        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.secondary,
                unselectedColor = unselectedRadioColor,
            ),
        )
    }
}