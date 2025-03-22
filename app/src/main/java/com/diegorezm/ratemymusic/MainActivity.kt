package com.diegorezm.ratemymusic

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.diegorezm.ratemymusic.RateMyMusicApp.Companion.appModule
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewFilter
import com.diegorezm.ratemymusic.modules.spotify_auth.domain.use_cases.getValidSpotifyAccessTokenUseCase
import com.diegorezm.ratemymusic.presentation.album.AlbumScreen
import com.diegorezm.ratemymusic.presentation.album.AlbumViewModel
import com.diegorezm.ratemymusic.presentation.auth.GoogleAuthUiClient
import com.diegorezm.ratemymusic.presentation.auth.sign_in.SignInScreen
import com.diegorezm.ratemymusic.presentation.auth.sign_in.SignInViewModel
import com.diegorezm.ratemymusic.presentation.auth.sign_up.SignUpScreen
import com.diegorezm.ratemymusic.presentation.auth.sign_up.SignUpViewModel
import com.diegorezm.ratemymusic.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.presentation.profile.ProfileViewModel
import com.diegorezm.ratemymusic.presentation.reviews.ReviewsViewModel
import com.diegorezm.ratemymusic.presentation.search.SearchViewModel
import com.diegorezm.ratemymusic.presentation.spotify_auth.SpotifyAuthScreen
import com.diegorezm.ratemymusic.presentation.track.TrackScreen
import com.diegorezm.ratemymusic.presentation.track.TrackViewModel
import com.diegorezm.ratemymusic.presentation.user_favorites.UserFavoritesViewModel
import com.diegorezm.ratemymusic.ui.theme.RateMyMusicTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    lateinit var googleAuthUiClient: GoogleAuthUiClient
    lateinit var signInViewModel: SignInViewModel
    lateinit var signUpViewModel: SignUpViewModel
    lateinit var profileViewModel: ProfileViewModel
    lateinit var searchViewModel: SearchViewModel
    lateinit var userFavoritesViewModel: UserFavoritesViewModel
    lateinit var trackViewModel: TrackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        googleAuthUiClient = GoogleAuthUiClient(this)

        signInViewModel = SignInViewModel(googleAuthUiClient, appModule.profileRepository)
        signUpViewModel = SignUpViewModel(googleAuthUiClient, appModule.profileRepository)

        userFavoritesViewModel = UserFavoritesViewModel(
            favoritesRepository = appModule.favoritesRepository,
            albumsRepository = appModule.albumsRepository,
            tracksRepository = appModule.tracksRepository,
            spotifTokenRepository = appModule.spotifyTokenRepository
        )

        profileViewModel = ProfileViewModel(
            profileRepository = appModule.profileRepository,
            spotifyTokenRepository = appModule.spotifyTokenRepository
        )

        searchViewModel =
            SearchViewModel(appModule.searchRpository, appModule.spotifyTokenRepository)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RateMyMusicTheme {
                var isUserAuthenticated by remember { mutableStateOf(false) }
                var isAuthLoading by remember { mutableStateOf(true) }

                var isSpotifyAuthenticated by remember { mutableStateOf(false) }
                var isSpotifyAuthLoading by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    isSpotifyAuthLoading = true
                    withContext(Dispatchers.IO) {
                        getValidSpotifyAccessTokenUseCase(appModule.spotifyTokenRepository).onSuccess {
                            isSpotifyAuthenticated = true
                        }.onFailure {
                            Log.e("MainActivity", "Error getting valid Spotify access token", it)
                            isSpotifyAuthenticated = false
                        }
                    }
                    isSpotifyAuthLoading = false
                }

                DisposableEffect(Unit) {
                    val authListener = FirebaseAuth.AuthStateListener { auth ->
                        isUserAuthenticated = auth.currentUser != null
                        isAuthLoading = false
                    }
                    FirebaseAuth.getInstance().addAuthStateListener(authListener)
                    onDispose {
                        FirebaseAuth.getInstance().removeAuthStateListener(authListener)
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    if (isAuthLoading || isSpotifyAuthLoading) {
                        LoadingIndicator()
                    } else {
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            enterTransition = {
                                slideInVertically(
                                    initialOffsetY = { -40 }
                                ) + expandVertically(
                                    expandFrom = Alignment.Top
                                ) + fadeIn(initialAlpha = 0.3f)
                            },
                            exitTransition = { slideOutVertically() + shrinkVertically() + fadeOut() },
                            startDestination = when {
                                !isUserAuthenticated && !isAuthLoading -> SignInRouteId
                                !isSpotifyAuthenticated && !isSpotifyAuthLoading -> SpotifyAuthRouteId
                                else -> MainAppRouteId
                            }
                        ) {
                            composable<MainAppRouteId> {
                                MainApp(
                                    navController,
                                    profileViewModel,
                                    searchViewModel,
                                    userFavoritesViewModel
                                )
                            }
                            composable<SpotifyAuthRouteId> {
                                SpotifyAuthScreen(navController)
                            }
                            composable<SignInRouteId> {
                                SignInScreen(viewModel = signInViewModel, navController)
                            }
                            composable<SignUpRouteId> {
                                SignUpScreen(navController, signUpViewModel)
                            }
                            composable<AlbumRouteId> {
                                val args = it.toRoute<AlbumRouteId>()

                                val albumViewModel =
                                    AlbumViewModel(
                                        appModule,
                                        args.albumId
                                    )

                                val filter = ReviewFilter.ByAlbum(args.albumId)
                                val reviewsViewModel =
                                    ReviewsViewModel(filter, appModule.reviewsRepository)

                                AlbumScreen(navController, albumViewModel, reviewsViewModel)
                            }

                            composable<TrackRouteId> {
                                val args = it.toRoute<TrackRouteId>()
                                trackViewModel =
                                    TrackViewModel(
                                        args.trackId,
                                        appModule.spotifyTokenRepository,
                                        appModule.reviewsRepository,
                                        appModule.tracksRepository,
                                        appModule.favoritesRepository
                                    )

                                val filter = ReviewFilter.ByTrack(args.trackId)
                                val reviewsViewModel =
                                    ReviewsViewModel(filter, appModule.reviewsRepository)

                                TrackScreen(navController, trackViewModel, reviewsViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Serializable
object SpotifyAuthRouteId

@Serializable
object MainAppRouteId

@Serializable
object SignInRouteId

@Serializable
object SignUpRouteId

@Serializable
data class AlbumRouteId(val albumId: String)

@Serializable
data class TrackRouteId(val trackId: String)


@Preview(showBackground = true)
@Composable
fun DarkColorSchemePreview() {
    RateMyMusicTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Dark Color Scheme Preview",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            ColorBox(
                "Background",
                MaterialTheme.colorScheme.background,
                MaterialTheme.colorScheme.onBackground
            )
            ColorBox(
                "Primary",
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.onPrimary
            )
            ColorBox(
                "Secondary",
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.onSecondary
            )
            ColorBox(
                "Tertiary",
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.onTertiary
            )
            ColorBox(
                "Surface",
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.onSurface
            )
            ColorBox("Error", MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.onError)
        }
    }
}

@Composable
fun ColorBox(label: String, color: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 4.dp)
            .background(color, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}
