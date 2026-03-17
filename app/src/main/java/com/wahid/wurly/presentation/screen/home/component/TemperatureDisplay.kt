package com.wahid.wurly.presentation.screen.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.wahid.wurly.R

/**
 * Displays the large temperature value and the weather condition text.
 *
 * Stateless — temperature & condition strings are injected.
 */
@Composable
fun TemperatureDisplay(
    modifier: Modifier = Modifier,
    temperature: String,
    condition: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = temperature + stringResource(R.string.weather_degree_symbol),
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 96.sp,
                fontWeight = FontWeight.Light,
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
        )
        Text(
            text = condition,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Normal,
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
}