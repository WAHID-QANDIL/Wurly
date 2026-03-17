package com.wahid.wurly.presentation.screen.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.wahid.wurly.presentation.navigation.DestinationRoutes

fun NavGraphBuilder.home(onNavigateToForecast: () -> Unit) {

    composable<DestinationRoutes.Home> {
        HomeEntry(onNavigateToForecast)
    }
}