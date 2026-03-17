package com.wahid.wurly.presentation.screen.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.wahid.wurly.R
import com.wahid.wurly.presentation.screen.alerts.component.NavItem

/**
 * Bottom navigation bar displaying a row of icon + label items.
 *
 * Stateless — selection state and click events are fully hoisted.
 */
@Composable
fun WeatherBottomNav(
    modifier: Modifier = Modifier,
    items: List<NavItem>,
    onItemClick: (index: Int) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.weather_nav_height))
            .clip(
                RoundedCornerShape(dimensionResource(R.dimen.weather_detail_card_corner_radius))
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEachIndexed { index, item ->
            WeatherNavItem(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                icon = item.icon,
                label = stringResource(item.labelRes),
                contentDescription = stringResource(item.contentDescriptionRes),
                isSelected = item.isSelected,
                onClick = { onItemClick(index) },
            )
        }
    }
}

/**
 * A single navigation item: icon + label, with selected/unselected states.
 */
@Composable
private fun WeatherNavItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.5f)
    val labelColor =
        if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.5f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.clickable(
            onClick = onClick,
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)),
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(dimensionResource(R.dimen.weather_nav_icon_size)),
            tint = tint,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.weather_detail_subtitle_top_spacing)))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Light,
            ),
            color = labelColor,
        )
    }
}