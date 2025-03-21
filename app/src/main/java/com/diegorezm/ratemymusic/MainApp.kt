package com.diegorezm.ratemymusic

import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.diegorezm.ratemymusic.presentation.components.BottomNavigationBar
import com.diegorezm.ratemymusic.presentation.home.HomeScreen
import com.diegorezm.ratemymusic.presentation.profile.ProfileScreen
import com.diegorezm.ratemymusic.presentation.profile.ProfileViewModel
import com.diegorezm.ratemymusic.presentation.search.SearchScreen
import com.diegorezm.ratemymusic.presentation.search.SearchViewModel
import kotlinx.serialization.Serializable

@Composable
fun MainApp(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    searchViewModel: SearchViewModel
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(bottomNavController)
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = MainRoutes.Home.route,
            modifier = Modifier.padding(paddingValues),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -40 }
                ) + expandHorizontally(expandFrom = Alignment.Start) + fadeIn(initialAlpha = 0.3f)
            },
            exitTransition = { slideOutHorizontally() + shrinkHorizontally() + fadeOut() },
        ) {
            composable(route = MainRoutes.Home.route) {
                HomeScreen()
            }
            composable(route = MainRoutes.Profile.route) {
                ProfileScreen(profileViewModel, navController)
            }
            composable(route = MainRoutes.Search.route) {
                SearchScreen(searchViewModel, navController)
            }
        }
    }
}

@Serializable
sealed class MainRoutes(val route: String) {
    @Serializable
    object Home : MainRoutes("HOME")

    @Serializable
    object Profile : MainRoutes("PROFILE")

    @Serializable
    object Search : MainRoutes("SEARCH")
}
