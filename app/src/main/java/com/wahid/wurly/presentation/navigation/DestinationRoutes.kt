package com.wahid.wurly.presentation.navigation

import kotlinx.serialization.Serializable
sealed interface DestinationRoutes {
    @Serializable
    data object Home: DestinationRoutes
    @Serializable
    data class Forecast(val cityId: Long? = null): DestinationRoutes
    @Serializable
    data object Settings: DestinationRoutes
    @Serializable
    data object Favorites: DestinationRoutes
    @Serializable
    data object AddFavorite: DestinationRoutes
    @Serializable
    data object Map: DestinationRoutes
    @Serializable
    data object Alerts: DestinationRoutes
}