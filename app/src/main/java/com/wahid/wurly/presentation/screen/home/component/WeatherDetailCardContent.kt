package com.wahid.wurly.presentation.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
 * A single weather detail metric card content (icon + label in header, value, subtitle).
 *
 * This composable renders only the **content** inside a card/glass box.
 * The container (GlassBox, Card, etc.) is the caller's responsibility,
 * adhering to the Open/Closed principle.
 */
@Composable
fun WeatherDetailCardContent(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconContentDescription: String,
    label: String,
    value: String,
    subtitle: String,
) {
    Column(
        modifier = modifier.padding(dimensionResource(R.dimen.weather_detail_card_padding)),
        verticalArrangement = Arrangement.Top,
    ) {
        // Icon + Label row
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = iconContentDescription,
                modifier = Modifier.size(dimensionResource(R.dimen.weather_detail_icon_size)),
                tint = Color.White.copy(alpha = 0.8f),
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.weather_detail_icon_spacing)))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                color = Color.White.copy(alpha = 0.8f),
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.weather_detail_value_top_spacing)))

        // Primary value
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = Color.White,
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.weather_detail_subtitle_top_spacing)))

        // Subtitle
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.6f),
        )
    }
}