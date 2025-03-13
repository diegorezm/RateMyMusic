package com.diegorezm.ratemymusic

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.diegorezm.ratemymusic.modules.spotify.SpotifyTokenManager

class SpotifyAuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri: Uri? = intent?.data
        val code = uri?.getQueryParameter("code")

        if (code != null) {
            SpotifyTokenManager.requestAccessToken(this, code)
        }
        
        finish()
    }
}