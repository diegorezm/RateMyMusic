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
import kotlinx.coroutines.launch

class SpotifyAuthActivity : ComponentActivity() {
    private lateinit var authLauncher: ActivityResultLauncher<Intent>

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
                    setResult(RESULT_OK, resultIntent)
                    finish()
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