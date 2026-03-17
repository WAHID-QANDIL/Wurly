package com.wahid.wurly.presentation.screen.map

import android.content.res.Configuration
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.ImageHolder
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.Marker
import com.mapbox.maps.extension.compose.style.GenericStyle
import com.mapbox.maps.extension.compose.style.rememberStyleState
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.location
import com.mrtdk.glass.GlassBox
import com.mrtdk.glass.GlassBoxScope
import com.mrtdk.glass.GlassContainer
import com.wahid.wurly.R
import com.wahid.wurly.presentation.common.model.LocationSuggestion
import com.wahid.wurly.presentation.common.rememberCachedBackgroundPainter
import com.wahid.wurly.presentation.screen.map.component.LocationSearchBar
import com.wahid.wurly.presentation.screen.map.component.LocationSuggestionRow
import com.wahid.wurly.presentation.screen.map.component.SaveLocationButton
import com.wahid.wurly.ui.theme.WurlyTheme

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    uiState: MapUiState = MapUiState.Loading,
    onEvent: (MapUiEvent) -> Unit,
) {
    when (uiState) {
        is MapUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is MapUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        is MapUiState.Success -> {
            MapContent(
                modifier = modifier,
                state = uiState,
                onEvent = onEvent,
            )
        }
    }
}


@Composable
private fun MapContent(
    modifier: Modifier = Modifier,
    state: MapUiState.Success,
    onEvent: (MapUiEvent) -> Unit,
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
                    .padding(top = topPadding)
                    .padding(bottom = dimensionResource(R.dimen.weather_nav_height)),
            ) {
                val columnScope = this

                Text(
                    text = stringResource(R.string.map_screen_title),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = sectionSpacing),
                    textAlign = TextAlign.Center,
                )


                with(glassScope) {
                    SearchSection(
                        query = state.searchQuery,
                        suggestions = state.suggestions,
                        onQueryChange = { onEvent(MapUiEvent.OnSearchQueryChange(it)) },
                        onClearClick = { onEvent(MapUiEvent.OnClearSearch) },
                        onSuggestionClick = {
                            onEvent(MapUiEvent.OnSuggestionClick(it))
                        },
                    )
                }

                Spacer(modifier = Modifier.height(sectionSpacing))


                with(glassScope) {
                    GlassBox {
                        with(glassScope) {
                            MapSection(
                                modifier = with(columnScope) {
                                    Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                },
                                selectedSuggestion = state.selectedSuggestion,
                                onMapTap = { lat, lon ->
                                    onEvent(MapUiEvent.OnMapTap(lat, lon))
                                }
                            )
                            SaveLocationButton(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(
                                        horizontal = saveButtonMargin,
                                        vertical = saveButtonBottom,
                                    ),
                                enabled = state.selectedSuggestion != null && !state.isSaving,
                                onClick = { onEvent(MapUiEvent.OnSaveLocation) },
                            )
                        }

                    }

                }
            }

        }
    }
}

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

@OptIn(MapboxExperimental::class)
@Composable
private fun GlassBoxScope.MapSection(
    modifier: Modifier = Modifier,
    selectedSuggestion: LocationSuggestion?,
    onMapTap: (Double, Double) -> Unit,
) {
    GlassBox(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.settings_card_corner_radius)),
        scale = 0.4f,
        darkness = 0.2f,
    ) {
        var markerPoint by remember { mutableStateOf<Point?>(null) }
        if (selectedSuggestion != null) {
            markerPoint = Point.fromLngLat(selectedSuggestion.longitude, selectedSuggestion.latitude)
        }

        val mapViewportState = rememberMapViewportState {
            setCameraOptions {
                zoom(2.0)
                center(Point.fromLngLat(-98.0, 39.5))
                pitch(0.0)
                bearing(0.0)
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            MapboxMap(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.settings_card_corner_radius))),
                mapViewportState = mapViewportState,
                style = {
                    @Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
                    GenericStyle(
                        styleState = rememberStyleState {
                            styleInteractionsState
                                .onMapClicked { context ->
                                    markerPoint = context.coordinateInfo.coordinate
                                    onMapTap(
                                        context.coordinateInfo.coordinate.latitude(),
                                        context.coordinateInfo.coordinate.longitude(),
                                    )
                                    true
                                }
                                .onMapLongClicked { context ->
                                    markerPoint = context.coordinateInfo.coordinate
                                    onMapTap(
                                        context.coordinateInfo.coordinate.latitude(),
                                        context.coordinateInfo.coordinate.longitude(),
                                    )
                                    true
                                }
                        },
                        style = Style.STANDARD,
                    )
                },
                scaleBar = { ScaleBar(Modifier.padding(top = 60.dp)) },
                logo = { Logo(Modifier.padding(bottom = 8.dp)) },
                attribution = { Attribution(Modifier.padding(bottom = 8.dp)) }

            ) {
                markerPoint?.let { point ->
                    Marker(point = point, text = point.toJson())
                }
                @Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
                MapEffect(Unit) { mapView ->
                    mapView.location.updateSettings {
                        enabled = true
                        puckBearing = PuckBearing.COURSE
                        puckBearingEnabled = true
                        locationPuck = LocationPuck2D(
                            topImage = ImageHolder.from(R.drawable.outline_map_pin_heart_24),
                            scaleExpression = interpolate {
                                linear()
                                zoom()
                                stop { literal(0.0); literal(0.6) }
                                stop { literal(20.0); literal(1.0) }
                            }.toJson()
                        )
                    }
                    mapViewportState.transitionToFollowPuckState()
                }
            }
        }
    }
}


// Previews

private fun previewState() = MapUiState.Success(
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
    name = "Map – Light",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun MapScreenLightPreview() {
    WurlyTheme {
        MapScreen(
            uiState = previewState(),
            onEvent = {},
        )
    }
}

@Preview(
    name = "Map – Dark",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun MapScreenDarkPreview() {
    WurlyTheme {
        MapScreen(
            uiState = previewState(),
            onEvent = {},
        )
    }
}