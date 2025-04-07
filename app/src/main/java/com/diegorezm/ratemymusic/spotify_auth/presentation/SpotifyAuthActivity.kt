package com.diegorezm.ratemymusic.spotify_auth.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.diegorezm.ratemymusic.BuildConfig
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.core.presentation.toUiText
import com.diegorezm.ratemymusic.spotify_auth.domain.SpotifyTokenRepository
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class SpotifyAuthActivity : ComponentActivity() {
    private lateinit var authLauncher: ActivityResultLauncher<Intent>
    private val spotifyTokenRepository =
        inject<SpotifyTokenRepository>().value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val response = AuthorizationClient.getResponse(result.resultCode, result.data)
            when (response.type) {
                AuthorizationResponse.Type.CODE -> {
                    val authCode = response.code
                    val resultIntent = Intent().apply {
                        putExtra("AUTH_CODE", authCode)
                    }
                    if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                spotifyTokenRepository.requestAccesToken(authCode)
                                    .onSuccess {
                                        setResult(RESULT_OK, resultIntent)
                                        finish()
                                    }
                                    .onError {
                                        Log.e(
                                            "SpotifyAuthActivity",
                                            "Error: ${it.name}"
                                        )
                                        showError(it.toUiText().toString())
                                        setResult(RESULT_CANCELED)
                                        finish()
                                    }
                            }
                        }
                    } else {
                        Log.w(
                            "SpotifyAuthActivity",
                            "Activity not in started state. Skipping Spotify Auth."
                        )
                        setResult(RESULT_CANCELED)
                        finish()
                    }
                }

                AuthorizationResponse.Type.ERROR -> {
                    Log.e("SpotifyAuthActivity", "Error: ${response.error}")

                    showError("Spotify login failed.")
                    setResult(RESULT_CANCELED)
                    finish()
                }

                else -> {
                    Log.e("SpotifyAuthActivity", "Unexpected response type: ${response.type}")
                    showError("Unexpected response")
                    setResult(RESULT_CANCELED)
                    finish()
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


}