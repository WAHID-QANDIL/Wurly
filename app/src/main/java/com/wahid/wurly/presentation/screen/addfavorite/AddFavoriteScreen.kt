package com.wahid.wurly.presentation.screen.addfavorite

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mrtdk.glass.GlassBox
import com.mrtdk.glass.GlassBoxScope
import com.mrtdk.glass.GlassContainer
import com.wahid.wurly.R
import com.wahid.wurly.presentation.common.rememberCachedBackgroundPainter
import com.wahid.wurly.presentation.common.model.LocationSuggestion
import com.wahid.wurly.presentation.screen.map.component.LocationSearchBar
import com.wahid.wurly.presentation.screen.map.component.LocationSuggestionRow
import com.wahid.wurly.presentation.screen.map.component.SaveLocationButton
import com.wahid.wurly.presentation.screen.settings.component.SettingsTopBar
import com.wahid.wurly.ui.theme.WurlyTheme

@Composable
fun AddFavoriteScreen(
    modifier: Modifier = Modifier,
    uiState: AddFavoriteUiState = AddFavoriteUiState.Loading,
    onEvent: (AddFavoriteUiEvent) -> Unit,
) {
    when (uiState) {
        is AddFavoriteUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is AddFavoriteUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        is AddFavoriteUiState.Success -> {
            AddFavoriteContent(
                modifier = modifier,
                state = uiState,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
private fun AddFavoriteContent(
    modifier: Modifier = Modifier,
    state: AddFavoriteUiState.Success,
    onEvent: (AddFavoriteUiEvent) -> Unit,
) {
    val horizontalPadding = dimensionResource(R.dimen.weather_screen_horizontal_padding)
    val topPadding = dimensionResource(R.dimen.weather_screen_top_padding)
    val sectionSpacing = dimensionResource(R.dimen.add_fav_section_spacing)
    val saveButtonMargin = dimensionResource(R.dimen.add_fav_save_button_horizontal_margin)
    val saveButtonBottom = dimensionResource(R.dimen.add_fav_save_button_bottom_margin)

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding)
                    .padding(top = topPadding),
            ) {
                val columnScope = this

                SettingsTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.add_favorite_title),
                    onBackClick = { onEvent(AddFavoriteUiEvent.OnBackClick) },
                )

                Spacer(modifier = Modifier.height(sectionSpacing))

                with(glassScope) {
                    SearchSection(
                        query = state.searchQuery,
                        suggestions = state.suggestions,
                        onQueryChange = { onEvent(AddFavoriteUiEvent.OnSearchQueryChange(it)) },
                        onClearClick = { onEvent(AddFavoriteUiEvent.OnClearSearch) },
                        onSuggestionClick = {
                            onEvent(AddFavoriteUiEvent.OnSuggestionClick(it))
                        },
                    )
                }

                Spacer(modifier = Modifier.height(sectionSpacing))

                with(glassScope) {
                    MapSection(
                        modifier = with(columnScope) {
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        },
                        selectedSuggestion = state.selectedSuggestion,
                    )
                }
            }

            SaveLocationButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        horizontal = saveButtonMargin,
                        vertical = saveButtonBottom,
                    ),
                enabled = state.selectedSuggestion != null && !state.isSaving,
                onClick = { onEvent(AddFavoriteUiEvent.OnSaveLocation) },
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Section composables
// ─────────────────────────────────────────────────────────────

@Composable
private fun GlassBoxScope.SearchSection(
    modifier: Modifier = Modifier,
    query: String,
    suggestions: List<LocationSuggestion>,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onSuggestionClick: (LocationSuggestion) -> Unit,
) {
    val cardCornerRadius = dimensionResource(R.dimen.add_fav_suggestion_corner_radius)
    val cardPadding = dimensionResource(R.dimen.favorites_card_padding)

    GlassBox(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(cardCornerRadius),
        scale = 0.6f,
        darkness = 0.35f,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(cardPadding),
        ) {
            LocationSearchBar(
                query = query,
                onQueryChange = onQueryChange,
                onClearClick = onClearClick,
            )

            if (suggestions.isNotEmpty()) {
                Spacer(
                    modifier = Modifier.height(
                        dimensionResource(R.dimen.add_fav_section_spacing)
                    )
                )
                suggestions.forEach { suggestion ->
                    LocationSuggestionRow(
                        displayName = suggestion.displayName,
                        highlightQuery = query,
                        onClick = { onSuggestionClick(suggestion) },
                    )
                }
            }
        }
    }
}

@Composable
private fun GlassBoxScope.MapSection(
    modifier: Modifier = Modifier,
    selectedSuggestion: LocationSuggestion?,
) {
    val pickLabelCorner = dimensionResource(R.dimen.add_fav_pick_label_corner_radius)
    val pickLabelHorizontalPadding = dimensionResource(R.dimen.add_fav_pick_label_horizontal_padding)
    val pickLabelVerticalPadding = dimensionResource(R.dimen.add_fav_pick_label_vertical_padding)

    GlassBox(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.settings_card_corner_radius)),
        scale = 0.4f,
        darkness = 0.2f,
    ) {
        // Map placeholder
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            // TODO: Replace with actual MapView composable
            Text(
                text = stringResource(R.string.add_favorite_map_cd),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
            )

            // ── "PICK THIS LOCATION" label ──
            if (selectedSuggestion != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(pickLabelCorner))
                        .background(Color.White)
                        .padding(
                            horizontal = pickLabelHorizontalPadding,
                            vertical = pickLabelVerticalPadding,
                        ),
                ) {
                    Text(
                        text = stringResource(R.string.add_favorite_pick_this_location),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = Color.Black,
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────

private fun previewState() = AddFavoriteUiState.Success(
    searchQuery = "San Fra",
    suggestions = listOf(
        LocationSuggestion(id = "1", displayName = "San Francisco, CA"),
        LocationSuggestion(id = "2", displayName = "San Francisco, Panama"),
    ),
    selectedSuggestion = null,
    isSaving = false,
    currentBackground = R.drawable.image1,
)

@Preview(
    name = "Add Favorite – Light",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun AddFavoriteScreenLightPreview() {
    WurlyTheme {
        AddFavoriteScreen(
            uiState = previewState(),
            onEvent = {},
        )
    }
}

@Preview(
    name = "Add Favorite – Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun AddFavoriteScreenDarkPreview() {
    WurlyTheme {
        AddFavoriteScreen(
            uiState = previewState(),
            onEvent = {},
        )
    }
}