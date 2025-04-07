package com.diegorezm.ratemymusic.spotify_auth.presentation

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.core.presentation.theme.RateMyMusicTheme
import com.diegorezm.ratemymusic.spotify_auth.presentation.ui.theme.SpotifyGreen

@Composable
fun SpotifyAuthScreen(onLoginSuccess: () -> Unit) {
    val context = LocalContext.current

    val authLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            onLoginSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.connect_to_your_spotify_account),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = stringResource(R.string.spotify_auth_description),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        val intent = Intent(context, SpotifyAuthActivity::class.java)
                        authLauncher.launch(intent)
                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonColors(
                        containerColor = SpotifyGreen,
                        contentColor = MaterialTheme.colorScheme.background,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.spotify_logo_black),
                        contentDescription = "Spotify Logo",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Connect to Spotify", fontWeight = FontWeight.Bold)
                }
            }

        }

        Image(
            painter = painterResource(id = R.drawable.undraw_happy_music),
            contentDescription = "Decorative Image",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .height(250.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SpotifyAuthScreenPreview() {
    RateMyMusicTheme {
        SpotifyAuthScreen(onLoginSuccess = {})
    }
}
