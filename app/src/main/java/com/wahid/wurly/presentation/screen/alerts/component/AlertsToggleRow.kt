package com.wahid.wurly.presentation.screen.alerts.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.wahid.wurly.R

/**
 * A toggle row with title, optional subtitle, and a trailing switch.
 *
 * Unlike [com.wahid.wurly.presentation.screen.settings.component.SettingsToggleRow] which has a leading icon, this variant
 * shows title + subtitle text only, matching the Alerts "Alerts Enabled" card.
 *
 * Stateless — the checked state and change callback are hoisted.
 */
@Composable
fun AlertsToggleRow(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                color = Color.White,
            )
            if (subtitle != null) {
                Spacer(
                    modifier = Modifier.width(
                        dimensionResource(R.dimen.weather_detail_subtitle_top_spacing)
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f),
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.secondary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.White.copy(alpha = 0.3f),
                uncheckedBorderColor = Color.Transparent,
            ),
        )
    }
}