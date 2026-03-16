package com.wahid.wurly.presentation.screen.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
 * A settings row with an icon, label + subtitle stacked, and trailing content
 * injected via a slot (e.g., segmented buttons or chips).
 *
 * Stateless — all data is injected.
 */
@Composable
fun SettingsLanguageRow(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconContentDescription: String,
    label: String,
    subtitle: String,
    trailing: @Composable () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconContentDescription,
            modifier = Modifier.size(dimensionResource(R.dimen.settings_row_icon_size)),
            tint = Color.White,
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.settings_row_icon_spacing)))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                ),
                color = Color.White,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f),
            )
        }

        trailing()
    }
}