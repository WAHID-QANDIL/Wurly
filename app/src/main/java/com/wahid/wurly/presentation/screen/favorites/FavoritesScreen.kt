package com.wahid.wurly.presentation.screen.favorites

import android.content.res.Configuration
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mrtdk.glass.GlassContainer
import com.wahid.wurly.R
import com.wahid.wurly.presentation.common.rememberCachedBackgroundPainter
import com.wahid.wurly.presentation.common.model.FavoriteLocationItem
import com.wahid.wurly.presentation.screen.favorites.component.FavoriteLocationCard
import com.wahid.wurly.presentation.screen.favorites.component.FavoritesTopBar
import com.wahid.wurly.ui.theme.WurlyTheme

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    uiState: FavoritesUiState = FavoritesUiState.Loading,
    onEvent: (FavoritesUiEvent) -> Unit,
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
                onEvent = onEvent,
            )
        }
    }
}


@Composable
private fun FavoritesContent(
    modifier: Modifier = Modifier,
    state: FavoritesUiState.Success,
    onEvent: (FavoritesUiEvent) -> Unit,
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
                        onBackClick = { onEvent(FavoritesUiEvent.OnBackClick) },
                        onSearchClick = { onEvent(FavoritesUiEvent.OnSearchClick) },
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
                            previewImageRes = item.previewImage,
                            onRemoveClick = {
                                onEvent(FavoritesUiEvent.OnRemoveFavorite(item.id))
                            },
                        )
                    }
                }

                item(key = "bottom_spacer") {
                    Spacer(modifier = Modifier.height(navHeight))
                }
            }

            FloatingActionButton(
                onClick = { onEvent(FavoritesUiEvent.OnAddFavoriteClick) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = fabMargin, bottom = navHeight + fabMargin),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.favorites_add_cd),
                )
            }
        }
    }
}


private fun previewFavorites() = listOf(
    FavoriteLocationItem(
        id = "1",
        cityName = "London, UK",
        temperature = "18°C",
        condition = "Cloudy",
        conditionIcon = Icons.Outlined.Cloud,
        previewImage = R.drawable.image1,
    ),
    FavoriteLocationItem(
        id = "2",
        cityName = "New York, USA",
        temperature = "24°C",
        condition = "Sunny",
        conditionIcon = Icons.Outlined.WbSunny,
        previewImage = R.drawable.image,
    ),
    FavoriteLocationItem(
        id = "3",
        cityName = "Tokyo, Japan",
        temperature = "21°C",
        condition = "Clear",
        conditionIcon = Icons.Outlined.WbSunny,
        previewImage = R.drawable.image1,
    ),
    FavoriteLocationItem(
        id = "4",
        cityName = "Paris, France",
        temperature = "16°C",
        condition = "Light Rain",
        conditionIcon = Icons.Outlined.WaterDrop,
        previewImage = R.drawable.image,
    ),
)

@Preview(
    name = "Favorites – Light",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun FavoritesScreenLightPreview() {
    WurlyTheme {
        FavoritesScreen(
            uiState = FavoritesUiState.Success(
                favorites = previewFavorites(),
                currentBackground = R.drawable.image,
            ),
            onEvent = {},
        )
    }
}

@Preview(
    name = "Favorites – Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun FavoritesScreenDarkPreview() {
    WurlyTheme {
        FavoritesScreen(
            uiState = FavoritesUiState.Success(
                favorites = previewFavorites(),
                currentBackground = R.drawable.image,
            ),
            onEvent = {},
        )
    }
}