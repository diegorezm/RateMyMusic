package com.diegorezm.ratemymusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.diegorezm.ratemymusic.presentation.auth.GoogleAuthUiClient
import com.diegorezm.ratemymusic.presentation.auth.sign_in.SignInScreen
import com.diegorezm.ratemymusic.presentation.auth.sign_in.SignInViewModel
import com.diegorezm.ratemymusic.presentation.auth.sign_up.SignUpScreen
import com.diegorezm.ratemymusic.presentation.auth.sign_up.SignUpViewModel
import com.diegorezm.ratemymusic.presentation.main.MainScreen
import com.diegorezm.ratemymusic.ui.theme.RateMyMusicTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RateMyMusicTheme {
                var isUserAuthenticated by remember { mutableStateOf(false) }

                DisposableEffect(Unit) {
                    val authListener = FirebaseAuth.AuthStateListener { auth ->
                        isUserAuthenticated = auth.currentUser != null
                    }
                    FirebaseAuth.getInstance().addAuthStateListener(authListener)

                    // Remove listener when the Composable is destroyed
                    onDispose {
                        FirebaseAuth.getInstance().removeAuthStateListener(authListener)
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = if (isUserAuthenticated) MainRouteId else SignInRouteId
                    ) {
                        composable<SignInRouteId> {
                            SignInPage(navController)
                        }
                        composable<MainRouteId> {
                            MainScreen(navController)
                        }
                        composable<SignUpRouteId> {
                            SignUpPage(navController)
                        }
                    }
                }
            }
        }
    }
}

@Serializable
object SignInRouteId

@Composable
fun SignInPage(navController: NavController) {
    val context = LocalContext.current
    val googleAuthUiClient = GoogleAuthUiClient(context)

    val signInViewModel = SignInViewModel(googleAuthUiClient)
    SignInScreen(viewModel = signInViewModel, navController)
}

@Serializable
object SignUpRouteId

@Composable
fun SignUpPage(navController: NavController) {
    val context = LocalContext.current
    val googleAuthUiClient = GoogleAuthUiClient(context)

    val signUpViewModel = SignUpViewModel(googleAuthUiClient)
    SignUpScreen(navController, signUpViewModel)
}

@Serializable
object MainRouteId


