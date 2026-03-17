package com.wahid.wurly.presentation.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mrtdk.glass.GlassBox
import com.mrtdk.glass.GlassBoxScope
import com.wahid.wurly.R

/**
 * A 2-column grid of weather detail cards rendered using GlassBox containers.
 *
 * Uses a static [Column] + [Row] layout instead of LazyVerticalGrid to avoid
 * nested-scrollable measuring exceptions when placed inside a scrollable parent.
 *
 * Must be called inside a [GlassBoxScope] so that GlassBox is available.
 */
@Composable
fun GlassBoxScope.WeatherDetailGrid(
    modifier: Modifier = Modifier,
    details: List<WeatherDetailItem>,
) {
    val cardCornerRadius = dimensionResource(R.dimen.weather_detail_card_corner_radius)
    val gridSpacing = dimensionResource(R.dimen.weather_detail_grid_spacing)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(gridSpacing),
    ) {
        val glassScope = this@WeatherDetailGrid
        details.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(gridSpacing),
            ) {
                val rowScope = this
                rowItems.forEach { item ->
                    with(glassScope) {
                        GlassBox(
                            modifier = with(rowScope) { Modifier.weight(1f) }
                                .height(130.dp),
                            shape = RoundedCornerShape(cardCornerRadius),
                            scale = 0.6f,
                            darkness = 0.35f,
                        ) {
                            WeatherDetailCardContent(
                                icon = item.icon,
                                iconContentDescription = stringResource(item.iconContentDescriptionRes),
                                label = stringResource(item.labelRes),
                                value = item.value,
                                subtitle = item.subtitle,
                            )
                        }
                    }
                }
                // If the last row has only 1 item, add an empty spacer to keep alignment
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}