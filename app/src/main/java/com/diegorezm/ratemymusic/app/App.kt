package com.diegorezm.ratemymusic.app

import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.auth.domain.AuthRepository
import com.diegorezm.ratemymusic.auth.presentation.sign_in.SignInScreenRoot
import com.diegorezm.ratemymusic.auth.presentation.sign_in.SignInViewModel
import com.diegorezm.ratemymusic.auth.presentation.sign_up.SignUpScreenRoot
import com.diegorezm.ratemymusic.auth.presentation.sign_up.SignUpViewModel
import com.diegorezm.ratemymusic.core.presentation.components.BottomNavigation
import com.diegorezm.ratemymusic.core.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.core.presentation.components.ScaffoldWithTopBar
import com.diegorezm.ratemymusic.core.presentation.theme.RateMyMusicTheme
import com.diegorezm.ratemymusic.followers.domain.FollowersRepository
import com.diegorezm.ratemymusic.home.presentation.HomeScreenRoot
import com.diegorezm.ratemymusic.music.albums.presentation.AlbumScreenRoot
import com.diegorezm.ratemymusic.music.artists.presentation.ArtistScreenRoot
import com.diegorezm.ratemymusic.music.search.presentation.SearchScreenRoot
import com.diegorezm.ratemymusic.music.tracks.presentation.TrackScreenRoot
import com.diegorezm.ratemymusic.profile.domain.repositories.ProfileRepository
import com.diegorezm.ratemymusic.profile.presentation.ProfileScreenRoot
import com.diegorezm.ratemymusic.profile.presentation.ProfileViewModel
import com.diegorezm.ratemymusic.reviews.presentation.ReviewFilter
import com.diegorezm.ratemymusic.reviews.presentation.ReviewViewModel
import com.diegorezm.ratemymusic.spotify_auth.presentation.SpotifyAuthScreenRoot
import com.diegorezm.ratemymusic.user_favorites.presentation.UserFavoritesViewModel
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
        isAuthLoading = when (sessionState) {
            is SessionStatus.Initializing -> true
            else -> false
        }

        isUserAuthenticated = when (sessionState) {
            is SessionStatus.Authenticated -> true
            else -> false
        }

    }


    RateMyMusicTheme {
        if (isAuthLoading) {
            LoadingIndicator()
        } else {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Route.AppGraph
                ) {
                    navigation<Route.AppGraph>(
                        startDestination = if (isUserAuthenticated) Route.MainRoutes else Route.SignIn
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

                        composable<Route.Profile> {
                            val args = it.toRoute<Route.Profile>().userId
                            val user = auth.currentUserOrNull()
                            if (user == null) {
                                navController.navigate(Route.SignIn)
                                return@composable
                            }

                            val profileRepository = koinInject<ProfileRepository>()
                            val followersRepository = koinInject<FollowersRepository>()
                            val authRepository = koinInject<AuthRepository>()

                            val profileViewModel = ProfileViewModel(
                                profileRepository,
                                followersRepository,
                                authRepository,
                                args,
                                user.id
                            )

                            val userFavoritesViewModel = UserFavoritesViewModel(
                                userFavoritesRepository = koinInject(),
                                albumRepository = koinInject(),
                                trackRepository = koinInject(),
                                profileId = args
                            )
                            ScaffoldWithTopBar(onBackClick = {
                                navController.navigateUp()
                            }, title = stringResource(R.string.profile_details)) {
                                ProfileScreenRoot(
                                    viewModel = profileViewModel,
                                    userFavoritesViewModel = userFavoritesViewModel,
                                    navController = navController
                                )
                            }

                        }

                        composable<Route.SpotifyAuth> {
                            SpotifyAuthScreenRoot(
                                onLoginSuccess = {
                                    navController.navigate(Route.MainRoutes)
                                }
                            )
                        }

                        composable<Route.AlbumDetails> {
                            val args = it.toRoute<Route.AlbumDetails>().albumId

                            val reviewsViewModel = ReviewViewModel(
                                filter = ReviewFilter.ByAlbum(args),
                                repository = koinInject(),
                                auth = auth
                            )

                            AlbumScreenRoot(
                                navController = navController,
                                reviewsViewModel = reviewsViewModel
                            )
                        }

                        composable<Route.AlbumDetails> {
                            ArtistScreenRoot(
                                navController = navController
                            )
                        }

                        composable<Route.TrackDetails> {
                            val args = it.toRoute<Route.TrackDetails>().trackId

                            val reviewsViewModel = ReviewViewModel(
                                filter = ReviewFilter.ByAlbum(args),
                                repository = koinInject(),
                                auth = auth
                            )

                            TrackScreenRoot(
                                navController = navController,
                                reviewsViewModel = reviewsViewModel
                            )
                        }
                    }
                }
            }
        }

    }


}


@Composable
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
                    HomeScreenRoot(onAlbumClick = {
                        val route = Route.AlbumDetails(it)
                        navController.navigate(route)
                    }, onUnauthorized = {
                        navController.navigate(Route.SpotifyAuth)
                    })
                }
                composable<MainRoute.Search> {
                    SearchScreenRoot(
                        navController = navController
                    )
                }
                composable<MainRoute.Profile> {
                    val auth = koinInject<Auth>()
                    val user = auth.currentUserOrNull()

                    if (user == null) {
                        navController.navigate(Route.SignIn)
                        return@composable
                    }

                    val profileRepository = koinInject<ProfileRepository>()
                    val followersRepository = koinInject<FollowersRepository>()
                    val authRepository = koinInject<AuthRepository>()

                    val profileViewModel =
                        ProfileViewModel(
                            profileRepository = profileRepository,
                            followersRepository = followersRepository,
                            authRepository = authRepository,
                            profileId = user.id,
                            currentUserId = user.id,
                            onSignOut = {
                                navController.navigate(Route.SignIn)
                            },

                            )

                    val userFavoritesViewModel = UserFavoritesViewModel(
                        userFavoritesRepository = koinInject(),
                        albumRepository = koinInject(),
                        trackRepository = koinInject(),
                        profileId = user.id
                    )

                    ProfileScreenRoot(
                        viewModel = profileViewModel,
                        userFavoritesViewModel = userFavoritesViewModel,
                        navController = navController
                    )
                }
            }

        }
    }

}


//@Composable
//private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
//    navController: NavController
//): T {
//    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
//    val parentEntry = remember(this) {
//        navController.getBackStackEntry(navGraphRoute)
//    }
//    return koinViewModel(
//        viewModelStoreOwner = parentEntry
//    )
//}