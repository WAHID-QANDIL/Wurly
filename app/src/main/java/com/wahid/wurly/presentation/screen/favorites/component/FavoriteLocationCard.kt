package com.wahid.wurly.presentation.screen.favorites.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import com.mrtdk.glass.GlassBox
import com.mrtdk.glass.GlassBoxScope
import com.wahid.wurly.R

/**
 * A single favorite location card rendered inside a GlassBox container.
 *
 * Displays city name, weather icon + temp · condition, a preview image,
 * and a remove (×) button.
 *
 * Must be called inside a [GlassBoxScope].
 */
@Composable
fun GlassBoxScope.FavoriteLocationCard(
    modifier: Modifier = Modifier,
    cityName: String,
    temperature: String,
    condition: String,
    conditionIcon: String,
    date: String,
    time: String,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit,
) {
    val cornerRadius = dimensionResource(R.dimen.favorites_card_corner_radius)

    GlassBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(cornerRadius),
        scale = 0.6f,
        darkness = 0.35f,
    ) {
        FavoriteLocationCardContent(
            cityName = cityName,
            temperature = temperature,
            condition = condition,
            conditionIcon = conditionIcon,
            date = date,
            time = time,
            onRemoveClick = onRemoveClick,
        )
    }
}

@Composable
private fun FavoriteLocationCardContent(
    modifier: Modifier = Modifier,
    cityName: String,
    temperature: String,
    condition: String,
    conditionIcon: String,
    date: String,
    time: String,
    onRemoveClick: () -> Unit,
) {
    val cardPadding = dimensionResource(R.dimen.favorites_card_padding)
    val previewSize = dimensionResource(R.dimen.favorites_preview_image_size)
    val previewCorner = dimensionResource(R.dimen.favorites_preview_corner_radius)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(cardPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = cityName,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                color = Color.White,
            )

            Text(
                text = "$date · $time",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.7f),
            )

            Text(
                text = stringResource(
                    R.string.favorites_temp_condition_format,
                    temperature,
                    condition,
                ),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
            )

        }

        AsyncImage(
            model = "https://openweathermap.org/img/wn/${conditionIcon}@2x.png",
            contentDescription = stringResource(
                R.string.favorites_preview_image_cd,
                cityName,
            ),
            modifier = Modifier
                .size(previewSize)
                .clip(RoundedCornerShape(previewCorner)),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.forecast_icon_text_spacing)))


        IconButton(
            onClick = onRemoveClick,
            modifier = Modifier.size(dimensionResource(R.dimen.favorites_remove_icon_size)),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.White.copy(alpha = 0.7f),
            ),
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(
                    R.string.favorites_remove_cd,
                    cityName,
                ),
            )
        }
    }
}