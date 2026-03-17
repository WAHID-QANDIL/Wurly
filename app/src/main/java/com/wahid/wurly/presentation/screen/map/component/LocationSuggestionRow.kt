package com.wahid.wurly.presentation.screen.map.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.wahid.wurly.R

/**
 * A single location suggestion row with a pin icon and the location name.
 *
 * The portion of [displayName] matching [highlightQuery] is rendered bold
 * to visually indicate the matched part.
 *
 * Stateless — the click callback is hoisted.
 */
@Composable
fun LocationSuggestionRow(
    modifier: Modifier = Modifier,
    displayName: String,
    highlightQuery: String,
    onClick: () -> Unit,
) {
    val iconSize = dimensionResource(R.dimen.add_fav_suggestion_icon_size)
    val iconSpacing = dimensionResource(R.dimen.add_fav_suggestion_icon_spacing)
    val verticalPadding = dimensionResource(R.dimen.add_fav_suggestion_padding)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = stringResource(R.string.add_favorite_suggestion_icon_cd),
            modifier = Modifier.size(iconSize),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
        )

        Spacer(modifier = Modifier.width(iconSpacing))

        Text(
            text = buildHighlightedText(displayName, highlightQuery),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * Builds an [AnnotatedString] where the first occurrence of [query]
 * (case-insensitive) within [text] is rendered bold.
 */
@Composable
private fun buildHighlightedText(
    text: String,
    query: String,
) = buildAnnotatedString {
    if (query.isBlank()) {
        append(text)
        return@buildAnnotatedString
    }
    val startIndex = text.lowercase().indexOf(query.lowercase())
    if (startIndex < 0) {
        append(text)
    } else {
        append(text.substring(0, startIndex))
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(text.substring(startIndex, startIndex + query.length))
        }
        append(text.substring(startIndex + query.length))
    }
}