package com.wahid.wurly.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Map
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mrtdk.glass.GlassContainer
import com.wahid.wurly.R
import com.wahid.wurly.presentation.screen.alerts.component.NavItem
import com.wahid.wurly.presentation.screen.home.component.WeatherBottomNav

/**
 * The four bottom navigation destinations and their corresponding routes.
 */
private data class BottomNavDestination(
    val route: DestinationRoutes,
    val item: NavItem,
)

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
) {
    val navHostController = rememberNavController()
    val backStackEntry by navHostController.currentBackStackEntryAsState()

    val bottomNavDestinations = remember {
        listOf(
            BottomNavDestination(
                route = DestinationRoutes.Home,
                item = NavItem(
                    icon = Icons.Filled.Home,
                    labelRes = R.string.nav_home,
                    contentDescriptionRes = R.string.nav_home_cd,
                ),
            ),
            BottomNavDestination(
                route = DestinationRoutes.Favorites,
                item = NavItem(
                    icon = Icons.Filled.Favorite,
                    labelRes = R.string.nav_favorites,
                    contentDescriptionRes = R.string.nav_favorites_cd,
                ),
            ),
            BottomNavDestination(
                route = DestinationRoutes.Map,
                item = NavItem(
                    icon = Icons.Outlined.Map,
                    labelRes = R.string.nav_map,
                    contentDescriptionRes = R.string.nav_map_cd,
                ),
            ),
            BottomNavDestination(
                route = DestinationRoutes.Alerts,
                item = NavItem(
                    icon = Icons.Filled.Notifications,
                    labelRes = R.string.nav_alerts,
                    contentDescriptionRes = R.string.nav_alerts_cd,
                ),
            ),
            BottomNavDestination(
                route = DestinationRoutes.Settings,
                item = NavItem(
                    icon = Icons.Filled.Settings,
                    labelRes = R.string.nav_settings,
                    contentDescriptionRes = R.string.nav_settings_cd,
                ),
            ),
        )
    }

    val selectedIndex by remember(backStackEntry) {
        derivedStateOf {
            val currentDestination = backStackEntry?.destination
            bottomNavDestinations.indexOfFirst { navDest ->
                currentDestination?.hasRoute(navDest.route::class) == true
            }.coerceAtLeast(0)
        }
    }

    val navItems = remember(selectedIndex) {
        bottomNavDestinations.mapIndexed { index, dest ->
            dest.item.copy(isSelected = index == selectedIndex)
        }
    }

    val cornerRadius = dimensionResource(R.dimen.weather_detail_card_corner_radius)

    GlassContainer(
        modifier = modifier.fillMaxSize(),
        content = {
            NavGraph(
                navHostController = navHostController,
                modifier = Modifier.fillMaxSize(),
            )
        },
    ) {
        WeatherBottomNav(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius)),
            items = navItems,
            onItemClick = { index ->
                val destination = bottomNavDestinations[index].route
                navHostController.navigate(destination) {
                    popUpTo(DestinationRoutes.Home) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
        )
    }
}