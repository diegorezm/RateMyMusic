package com.diegorezm.ratemymusic.presentation.spotify_auth

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.MainAppRouteId
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.SpotifyAuthActivity
import com.diegorezm.ratemymusic.ui.theme.SpotifyGreen


@Composable
fun SpotifyAuthScreen(navController: NavController) {
    val context = LocalContext.current

    val authLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            navController.navigate(MainAppRouteId)
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

            Text(
                text = "Connect Your Spotify Account",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "To start using this application, first you need to connect to your spotify account!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(
                onClick = {
                    val intent = Intent(context, SpotifyAuthActivity::class.java)
                    authLauncher.launch(intent)
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonColors(
                    containerColor = SpotifyGreen,
                    contentColor = MaterialTheme.colorScheme.onBackground,
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

        // Image at the bottom-right corner
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
