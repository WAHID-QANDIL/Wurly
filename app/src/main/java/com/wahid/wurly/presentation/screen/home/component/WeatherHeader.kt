package com.wahid.wurly.presentation.screen.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.outlined.CloudQueue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wahid.wurly.R

/**
 * Top header with a back button, location name, and date/time subtitle.
 *
 * Stateless — all data is injected, back-click is hoisted.
 */
@Composable
fun WeatherHeader(
    modifier: Modifier = Modifier,
    locationName: String,
    dateTime: String,
    onNavigateToForecast: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        LocationLabel(
            locationName = locationName,
            dateTime = dateTime,
        )

        Icon(
            imageVector = Icons.Filled.Cloud,
            contentDescription = stringResource(R.string.navigate_to_forecast),
            tint = Color.White,
            modifier = Modifier.size(32.dp),
        )

        Text(
            text = stringResource(R.string.weather_forecast_screen),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Normal,
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable(true) {
                onNavigateToForecast()
            }
        )
    }
}

/**
 * Displays the location name and date/time, stacked vertically.
 */
@Composable
private fun LocationLabel(
    modifier: Modifier = Modifier,
    locationName: String,
    dateTime: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = locationName,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
        )
        Text(
            text = dateTime,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing,
            ),
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
        )
    }
}