package com.diegorezm.ratemymusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.diegorezm.ratemymusic.ui.theme.RateMyMusicTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var authManager: AuthManager
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        authManager = AuthManager(firebaseAuth)
        setContent {
            RateMyMusicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val isAuthenticated by authManager.isUserAuthenticated.collectAsState()
                    if (isAuthenticated) {
                        MainScreen()
                    } else {
                        AuthScreen(authManager)
                    }
                }
            }
        }
    }
}

class AuthManager(private val firebaseAuth: FirebaseAuth) {

    private val _isUserAuthenticated = MutableStateFlow<Boolean>(false)
    val isUserAuthenticated: StateFlow<Boolean> = _isUserAuthenticated
    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        firebaseAuth.addAuthStateListener { auth ->
            scope.launch {
                _isUserAuthenticated.value = auth.currentUser != null
            }

        }
    }
}

