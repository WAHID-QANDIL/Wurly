package com.wahid.wurly.presentation.screen.alerts.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import com.mrtdk.glass.GlassBox
import com.mrtdk.glass.GlassBoxScope
import com.wahid.wurly.R

/**
 * A single active weather alert row rendered inside a GlassBox container.
 *
 * Displays an icon, title with description subtitle, timestamp,
 * and a Stop (×) button that cancels / removes the alert.
 *
 * Must be called inside a [GlassBoxScope].
 */
@Composable
fun GlassBoxScope.AlertListItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    description: String,
    timestamp: String,
    onStopClick: () -> Unit,
) {
    val cornerRadius = dimensionResource(R.dimen.alerts_item_corner_radius)

    GlassBox(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(cornerRadius),
        scale = 0.6f,
        darkness = 0.35f,
    ) {
        AlertListItemContent(
            icon = icon,
            title = title,
            description = description,
            timestamp = timestamp,
            onStopClick = onStopClick,
        )
    }
}

@Composable
private fun AlertListItemContent(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    description: String,
    timestamp: String,
    onStopClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.alerts_item_padding)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(R.string.alerts_item_icon_cd),
            modifier = Modifier.size(dimensionResource(R.dimen.alerts_row_icon_size)),
            tint = MaterialTheme.colorScheme.secondary,
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.alerts_row_icon_spacing)))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                color = Color.White,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
            )
            Text(
                text = timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f),
            )
        }

        IconButton(
            onClick = onStopClick,
            modifier = Modifier.size(dimensionResource(R.dimen.favorites_remove_icon_size)),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.error.copy(alpha = 0.85f),
            ),
        ) {
            Icon(
                imageVector = Icons.Filled.StopCircle,
                contentDescription = stringResource(R.string.alerts_stop_cd, title),
            )
        }
    }
}