package com.diegorezm.ratemymusic.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.diegorezm.ratemymusic.MainRoute
import com.diegorezm.ratemymusic.core.presentation.theme.RateMyMusicTheme

data class BottomNavItem(val label: String, val icon: ImageVector, val route: MainRoute)


@Composable
fun BottomNavigation(
    navController: NavController
) {
    val items = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Default.Home,
            route = MainRoute.Home
        ),
        BottomNavItem(
            label = "Search",
            icon = Icons.Default.Search,
            route = MainRoute.Search
        ),
        BottomNavItem(
            label = "Profile",
            icon = Icons.Default.Person,
            route = MainRoute.Profile
        )
    )

    Column(modifier = Modifier.fillMaxWidth()) {

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            2.dp,
            MaterialTheme.colorScheme.primary
        )
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination


            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    label = {
                        Text(
                            text = item.route.javaClass.simpleName
                        )
                    },
                    selected = currentDestination?.route == item.route.javaClass.simpleName,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(MainRoute.Home) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    colors = NavigationBarItemColors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        selectedIndicatorColor = MaterialTheme.colorScheme.secondary,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f),
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f),
                        disabledIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    ),
                    modifier = Modifier.clip(MaterialTheme.shapes.medium)
                )
            }
        }
    }

}


@Composable
@Preview(showBackground = true)
private fun BottomNavigationBarPreview() {
    val navController = rememberNavController()
    RateMyMusicTheme {
        BottomNavigation(navController)
    }
}
