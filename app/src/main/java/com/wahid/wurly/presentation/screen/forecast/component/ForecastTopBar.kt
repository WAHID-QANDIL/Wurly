package com.wahid.wurly.presentation.screen.forecast.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.wahid.wurly.R

/**
 * Top bar with back button, centered title, and overflow menu button.
 *
 * Stateless — all actions are hoisted via callbacks.
 */
@Composable
fun ForecastTopBar(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.forecast_title),
    onBackClick: () -> Unit,
    onOverflowClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(dimensionResource(R.dimen.weather_back_button_size)),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.White,
            ),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.weather_back_button_cd),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = onOverflowClick,
            modifier = Modifier.size(dimensionResource(R.dimen.weather_back_button_size)),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.White,
            ),
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = stringResource(R.string.forecast_overflow_cd),
            )
        }
    }
}