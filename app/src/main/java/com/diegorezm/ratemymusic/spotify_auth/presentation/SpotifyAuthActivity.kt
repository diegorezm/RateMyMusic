package com.diegorezm.ratemymusic.spotify_auth.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.diegorezm.ratemymusic.BuildConfig
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SpotifyAuthActivity : ComponentActivity() {
    private lateinit var authLauncher: ActivityResultLauncher<Intent>

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("SpotifyAuthActivity", "onCreate called")
        Log.d("SpotifyAuthActivity", "Received intent: ${intent?.data?.toString()}")
        Log.d("SpotifyAuthActivity", "Intent action: ${intent?.action}")

        authLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val response = AuthorizationClient.getResponse(result.resultCode, result.data)
                when (response.type) {
                    AuthorizationResponse.Type.CODE -> {
                        val authCode = response.code
                        Log.d("SpotifyAuthActivity", "Authorization Code Received: $authCode")
                        _authState.value = AuthState.Success(authCode)
                    }

                    AuthorizationResponse.Type.ERROR -> {
                        Log.e("SpotifyAuthActivity", "Error: ${response.error}")
                        showError("Spotify login failed.")
                        _authState.value = AuthState.Error("Spotify login failed.")
                    }

                    else -> {
                        Log.e("SpotifyAuthActivity", "Unexpected response type: ${response.type}")
                        showError("Unexpected response")
                        _authState.value = AuthState.Error("Unexpected response")
                    }
                }
            } else {
                Log.e(
                    "SpotifyAuthActivity",
                    "Result was not OK or data was null. ResultCode: ${result.resultCode}, Data: ${result.data}"
                )
                showError("Failed to get authorization result.")
                _authState.value = AuthState.Error("Failed to get authorization result.")
            }
        }

        lifecycleScope.launch {
            authState.collect { state ->
                when (state) {
                    is AuthState.Success -> {
                        val resultIntent = Intent().apply {
                            putExtra("AUTH_CODE", state.authCode)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }

                    is AuthState.Error -> {
                        setResult(RESULT_CANCELED)
                        finish()
                    }

                    AuthState.Idle -> {

                    }
                }
            }
        }


        lifecycleScope.launch {
            sendLoginRequest()
        }
    }

    private fun sendLoginRequest() {
        val redirectURI = "com.diegorezm.ratemymusic://callback"
        val scopes = arrayOf("user-read-private", "user-read-email")
        val spotifyClientId = BuildConfig.SPOTIFY_CLIENT_ID

        val request = AuthorizationRequest.Builder(
            spotifyClientId,
            AuthorizationResponse.Type.CODE,
            redirectURI
        ).setScopes(scopes).build()

        val intent = AuthorizationClient.createLoginActivityIntent(this, request)
        authLauncher.launch(intent)
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    sealed class AuthState {
        object Idle : AuthState()
        data class Success(val authCode: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}

