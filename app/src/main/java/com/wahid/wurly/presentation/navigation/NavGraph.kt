package com.wahid.wurly.presentation.navigation

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.wahid.wurly.presentation.screen.home.home
import com.wahid.wurly.presentation.screen.forecast.forecast
import com.wahid.wurly.presentation.screen.settings.settings
import com.wahid.wurly.presentation.screen.favorites.favorites
import com.wahid.wurly.presentation.screen.alerts.alerts
import com.wahid.wurly.presentation.screen.map.map


private const val TRANSITION_DURATION = 600

@Composable
fun NavGraph(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = DestinationRoutes.Home,
        enterTransition = {
            fadeIn(
                animationSpec = tween(TRANSITION_DURATION, easing = FastOutLinearInEasing)
            )
        }, exitTransition = {
            fadeOut(
                animationSpec = tween(TRANSITION_DURATION, easing = FastOutLinearInEasing)
            )
        }
    ) {
        home(onNavigateToForecast = {
                navHostController.navigate(DestinationRoutes.Forecast())
        })
        forecast(onBackClick = {
             navHostController.navigateUp()
        })
        settings()
        favorites(onFavoriteClick = { cityId ->
             navHostController.navigate(DestinationRoutes.Forecast(cityId = cityId))
        })
        map()
        alerts()
    }

}