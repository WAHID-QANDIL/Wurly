package com.wahid.wurly.presentation.screen.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.mrtdk.glass.GlassContainer
import com.wahid.wurly.R
import com.wahid.wurly.presentation.common.rememberCachedBackgroundPainter
import com.wahid.wurly.presentation.screen.favorites.component.FavoriteLocationCard
import com.wahid.wurly.presentation.screen.favorites.component.FavoritesTopBar

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    uiState: FavoritesUiState = FavoritesUiState.Loading,
    onFavoriteClick: (Long) -> Unit,
    onRemoveFavorite: (String) -> Unit,
) {
    when (uiState) {
        is FavoritesUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is FavoritesUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        is FavoritesUiState.Success -> {
            FavoritesContent(
                modifier = modifier,
                state = uiState,
                onFavoriteClick = onFavoriteClick,
                onRemoveFavorite = onRemoveFavorite,
            )
        }
    }
}


@Composable
private fun FavoritesContent(
    modifier: Modifier = Modifier,
    state: FavoritesUiState.Success,
    onFavoriteClick: (Long) -> Unit,
    onRemoveFavorite: (String) -> Unit,
) {
    val horizontalPadding = dimensionResource(R.dimen.weather_screen_horizontal_padding)
    val topPadding = dimensionResource(R.dimen.weather_screen_top_padding)
    val cardSpacing = dimensionResource(R.dimen.favorites_card_spacing)
    val listTopSpacing = dimensionResource(R.dimen.favorites_list_top_spacing)
    val navHeight = dimensionResource(R.dimen.weather_nav_height)
    val fabMargin = dimensionResource(R.dimen.favorites_fab_margin)

    GlassContainer(
        modifier = modifier.fillMaxSize(),
        content = {
            Image(
                painter = rememberCachedBackgroundPainter(state.currentBackground),
                contentDescription = stringResource(R.string.weather_background_cd),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        },
    ) {
        val glassScope = this

        Box(modifier = Modifier.fillMaxSize()) {
            if (state.favorites.isEmpty()) {

                Text(
                    text = stringResource(R.string.favorites_empty),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = horizontalPadding),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding)
                    .padding(top = topPadding),
                verticalArrangement = Arrangement.spacedBy(cardSpacing),
            ) {
                item(key = "top_bar") {
                    FavoritesTopBar(
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(listTopSpacing))
                }

                items(
                    items = state.favorites,
                    key = { it.id },
                ) { item ->
                    with(glassScope) {
                        FavoriteLocationCard(
                            cityName = item.cityName,
                            temperature = item.temperature,
                            condition = item.condition,
                            conditionIcon = item.conditionIcon,
                            date = item.date,
                            time = item.time,
                            onClick = { onFavoriteClick(item.id.toLong()) },
                            onRemoveClick = {
                                onRemoveFavorite(item.id)
                            },
                        )
                    }
                }

                item(key = "bottom_spacer") {
                    Spacer(modifier = Modifier.height(navHeight))
                }
            }
         }
     }
 }