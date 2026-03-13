package com.wahid.wurly.presentation.screen.home

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.wahid.wurly.presentation.navigation.DestinationRoutes

fun NavGraphBuilder.home() {

    composable<DestinationRoutes.Home> {
        val homeViewModel = hiltViewModel<HomeViewModel>()
        val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
        HomeScreen(
            uiState = uiState,
            onEvent = homeViewModel::onAction
        )
    }
}
/*
uiState = HomeUiState.Success(
locationName = "Cairo",
dateTime = "2026-3-1",
temperature = "23",
condition = "Cold",
details = listOf(
WeatherDetailItem(
icon = Icons.Outlined.WaterDrop,
iconContentDescriptionRes = R.string.weather_icon_uv_cd,
labelRes = R.string.nav_home,
value = "23",
subtitle = "This is something"
)
) ,
currentBackground = R.drawable.image
)*/