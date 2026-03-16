package com.wahid.wurly.presentation.screen.forecast.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mrtdk.glass.GlassBox
import com.mrtdk.glass.GlassBoxScope
import com.wahid.wurly.R
import com.wahid.wurly.presentation.common.model.ForecastCardItem

/**
 * Horizontal list of compact forecast cards. Designed to be reusable (Home, Forecast, etc.).
 * Must be invoked inside a [GlassBoxScope].
 */
@Composable
fun GlassBoxScope.ForecastCardRow(
    items: List<ForecastCardItem>,
    modifier: Modifier = Modifier,
) {
    val cardCornerRadius = dimensionResource(R.dimen.forecast_row_corner_radius)
    val cardSpacing = dimensionResource(R.dimen.forecast_row_spacing)

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(cardSpacing),
    ) {
        items(
            items = items,
            key = { it.timeLabel },
        ) { item ->
            GlassBox(
                modifier = Modifier.size(width = 140.dp, height = 160.dp),
                shape = RoundedCornerShape(cardCornerRadius),
                scale = 0.6f,
                darkness = 0.35f,
            ) {
                ForecastCardContent(item = item)
            }
        }
    }
}

@Composable
private fun ForecastCardContent(item: ForecastCardItem) {
    Column(
        modifier = Modifier.padding(dimensionResource(R.dimen.forecast_row_horizontal_padding)),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = item.timeLabel,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White,
        )


        AsyncImage(
            model = "https://openweathermap.org/payload/api/media/file/${item.icon}.png",
            contentDescription = stringResource(R.string.forecast_card_icon_cd, item.timeLabel),
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Fit
        )

        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = item.temperature,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
            )
        }
    }
}