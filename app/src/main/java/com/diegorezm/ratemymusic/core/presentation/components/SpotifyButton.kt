package com.diegorezm.ratemymusic.core.presentation.components

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.core.presentation.theme.RateMyMusicTheme

@Composable
fun SpotifyLinkButton(externalURL: String) {
    val context = LocalContext.current
    OutlinedButton(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, externalURL.toUri())
            context.startActivity(intent)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.spotify_logo),
                contentDescription = "Spotify Logo",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Listen on Spotify",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

    }
}

@Composable
@Preview(showBackground = false, name = "SpotifyLinkButton")
private fun SpotifyButtonPreview() {
    RateMyMusicTheme {
        SpotifyLinkButton("")
    }
}
