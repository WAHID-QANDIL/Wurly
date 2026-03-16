package com.wahid.wurly.presentation.screen.map.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.wahid.wurly.R

/**
 * A glass-style search bar with a search icon, text input, and an optional
 * clear button.
 *
 * Stateless — [query] and [onQueryChange] are hoisted.
 */
@Composable
fun LocationSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
) {
    val cornerRadius = dimensionResource(R.dimen.add_fav_search_corner_radius)
    val horizontalPadding = dimensionResource(R.dimen.add_fav_search_horizontal_padding)
    val verticalPadding = dimensionResource(R.dimen.add_fav_search_vertical_padding)
    val iconSize = dimensionResource(R.dimen.add_fav_search_icon_size)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color.White.copy(alpha = 0.2f))
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(R.string.add_favorite_search_cd),
            modifier = Modifier.size(iconSize),
            tint = Color.White.copy(alpha = 0.7f),
        )

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.add_fav_suggestion_icon_spacing)))

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.primary,
            ),
            cursorBrush = SolidColor(Color.White),
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        text = stringResource(R.string.add_favorite_search_hint),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                    )
                }
                innerTextField()
            },
        )

        if (query.isNotEmpty()) {
            IconButton(
                onClick = onClearClick,
                modifier = Modifier.size(iconSize),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                ),
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.add_favorite_clear_cd),
                )
            }
        }
    }
}