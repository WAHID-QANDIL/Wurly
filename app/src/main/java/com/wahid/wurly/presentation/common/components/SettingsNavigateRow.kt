package com.wahid.wurly.presentation.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.wahid.wurly.R

/**
 * A settings row with an icon, label text, and a trailing chevron arrow.
 *
 * Clicking the row triggers [onClick].
 * Stateless — the click action is hoisted.
 */
@Composable
fun SettingsNavigateRow(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconContentDescription: String,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconContentDescription,
            modifier = Modifier.size(dimensionResource(R.dimen.settings_row_icon_size)),
            tint = Color.White,
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.settings_row_icon_spacing)))

        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
            ),
            color = Color.White,
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(R.string.settings_chevron_cd),
            modifier = Modifier.size(dimensionResource(R.dimen.settings_row_icon_size)),
            tint = Color.White.copy(alpha = 0.6f),
        )
    }
}