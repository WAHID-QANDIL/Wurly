package com.wahid.wurly.presentation.screen.forecast.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import com.mrtdk.glass.GlassBox
import com.mrtdk.glass.GlassBoxScope
import com.wahid.wurly.R

/**
 * A single forecast day row rendered inside a GlassBox container.
 *
 * Displays the weather icon, day name with condition subtitle,
 * and high/low temperatures.
 *
 * Must be called inside a [GlassBoxScope].
 */
@Composable
fun GlassBoxScope.ForecastDayRow(
    modifier: Modifier = Modifier,
    dayName: String,
    condition: String,
    conditionIcon: String,
    highTemp: String,
    lowTemp: String,
    date: String,
    time: String,
) {
    val cornerRadius = dimensionResource(R.dimen.forecast_row_corner_radius)

    GlassBox(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(cornerRadius),
        scale = 0.6f,
        darkness = 0.35f,
    ) {
        ForecastDayRowContent(
            dayName = dayName,
            condition = condition,
            conditionIcon = conditionIcon,
            highTemp = highTemp,
            lowTemp = lowTemp,
            date = date,
            time = time,
        )
    }
}

/**
 * Inner content of a forecast day row.
 *
 * Separated from the glass container so it can be reused in other contexts.
 */
@Composable
private fun ForecastDayRowContent(
    modifier: Modifier = Modifier,
    dayName: String,
    condition: String,
    conditionIcon: String,
    highTemp: String,
    lowTemp: String,
    date: String,
    time: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(R.dimen.forecast_row_horizontal_padding),
                vertical = dimensionResource(R.dimen.forecast_row_vertical_padding),
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${conditionIcon}@2x.png",
            contentDescription = stringResource(R.string.forecast_day_icon_cd, dayName),
            modifier = Modifier.size(dimensionResource(R.dimen.forecast_icon_size)),
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.forecast_icon_text_spacing)))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = dayName,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                color = Color.White,
            )
            Text(
                text = condition,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
            )
            Text(
                text = "$date $time",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
            )
        }

        Text(
            text = stringResource(R.string.forecast_temp_format, highTemp, lowTemp),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
            ),
            color = Color.White,
        )
    }
}