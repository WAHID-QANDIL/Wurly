package com.wahid.wurly.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.wahid.wurly.R

/**
 * Represents a single option in a segmented button group.
 */
data class SegmentedOption<T>(
    val value: T,
    val label: String,
)

/**
 * A segmented button group — a row of chips where exactly one is selected.
 *
 * The selected chip is rendered with an accent (orange) background.
 * Unselected chips are transparent with white text.
 *
 * Stateless — [selectedValue] and [onOptionSelected] are hoisted.
 *
 * @param T The type of value each option represents (e.g., an enum).
 */
@Composable
fun <T> SettingsSegmentedButtons(
    modifier: Modifier = Modifier,
    options: List<SegmentedOption<T>>,
    selectedValue: T,
    onOptionSelected: (T) -> Unit,
) {
    val chipCornerRadius = dimensionResource(R.dimen.settings_chip_corner_radius)
    val chipHorizontalPadding = dimensionResource(R.dimen.settings_chip_horizontal_padding)
    val chipVerticalPadding = dimensionResource(R.dimen.settings_chip_vertical_padding)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.settings_chip_spacing)),
    ) {
        options.forEach { option ->
            val isSelected = option.value == selectedValue
            val shape = RoundedCornerShape(chipCornerRadius)

            Box(
                modifier = Modifier
                    .clip(shape)
                    .background(
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.secondary
                        } else {
                            Color.Transparent
                        },
                        shape = shape,
                    )
                    .clickable { onOptionSelected(option.value) }
                    .padding(
                        horizontal = chipHorizontalPadding,
                        vertical = chipVerticalPadding,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = option.label,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    ),
                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                )
            }
        }
    }
}