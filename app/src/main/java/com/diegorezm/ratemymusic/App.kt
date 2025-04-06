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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.diegorezm.ratemymusic.auth.presentation.sign_in.SignInScreenRoot
import com.diegorezm.ratemymusic.auth.presentation.sign_in.SignInViewModel
import com.diegorezm.ratemymusic.auth.presentation.sign_up.SignUpScreenRoot
import com.diegorezm.ratemymusic.auth.presentation.sign_up.SignUpViewModel
import com.diegorezm.ratemymusic.core.presentation.components.BottomNavigation
import com.diegorezm.ratemymusic.core.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.core.presentation.theme.RateMyMusicTheme
import com.diegorezm.ratemymusic.home.presentation.HomeScreen
import com.diegorezm.ratemymusic.profile.presentation.ProfileScreen
import com.diegorezm.ratemymusic.search.presentation.SearchScreen
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.status.SessionStatus
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun App() {
    val navController = rememberNavController()
    val auth = koinInject<Auth>()

    var isUserAuthenticated by remember { mutableStateOf(false) }
    var isAuthLoading by remember { mutableStateOf(true) }

    val sessionState by auth.sessionStatus.collectAsState()


    LaunchedEffect(sessionState) {
        isUserAuthenticated = when (sessionState) {
            is SessionStatus.Authenticated -> true
            else -> false
        }
        isAuthLoading = when (sessionState) {
            is SessionStatus.Initializing -> true
            else -> false

        }
    }


    RateMyMusicTheme {
        if (isAuthLoading) {
            LoadingIndicator()
        } else {
            NavHost(
                navController = navController,
                startDestination = Route.AppGraph
            ) {
                navigation<Route.AppGraph>(
                    startDestination = if (isUserAuthenticated) Route.MainRoutes else Route.SignUp
                ) {
                    composable<Route.MainRoutes> {
                        MainRoutesScreen(navController)
                    }

                    composable<Route.SignUp> {
                        val viewModel = koinViewModel<SignUpViewModel>()
                        SignUpScreenRoot(
                            modifier = Modifier,
                            viewModel = viewModel,
                            onSuccessfulSignUp = {
                                navController.navigate(Route.MainRoutes)
                            },
                            onAlreadyHaveAccount = {
                                navController.navigate(Route.SignIn)
                            }
                        )
                    }

                    composable<Route.SignIn> {
                        val viewModel = koinViewModel<SignInViewModel>()
                        SignInScreenRoot(
                            modifier = Modifier,
                            viewModel = viewModel,
                            onSuccessfulSignIn = {
                                navController.navigate(Route.MainRoutes)
                            },
                            onNoAccountClick = {
                                navController.navigate(Route.SignUp)
                            }
                        )
                    }
                }
            }
        }
    }


}


@Composable
@Preview
private fun MainRoutesScreen(
    navController: NavController = rememberNavController()
) {
    val bottomNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(bottomNavController)
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = MainRoute.MainRouteGraph,
            modifier = Modifier.padding(paddingValues),
            enterTransition = {
                slideInHorizontally() + expandHorizontally(expandFrom = Alignment.Start) + fadeIn(
                    initialAlpha = 0.3f
                )
            },
            exitTransition = { slideOutHorizontally() + shrinkHorizontally() + fadeOut() },
        ) {
            navigation<MainRoute.MainRouteGraph>(
                startDestination = MainRoute.Home
            ) {
                composable<MainRoute.Home> {
                    HomeScreen()
                }
                composable<MainRoute.Search> {
                    SearchScreen()
                }
                composable<MainRoute.Profile> {
                    ProfileScreen()
                }
            }

        }
    }

}


@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}