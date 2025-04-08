package com.diegorezm.ratemymusic.music.albums.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.core.presentation.components.SpotifyLinkButton
import com.diegorezm.ratemymusic.core.presentation.formatTimestamp
import com.diegorezm.ratemymusic.music.albums.domain.Album

@Composable
fun AlbumDetails(
    album: Album,
) {
    val formattedDate = formatTimestamp(album.releaseDate)
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = album.imageURL,
                contentDescription = "Album Cover",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillBounds
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = album.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = album.artists.joinToString(", ") { it.name },
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center

        )

        Text(
            text = formattedDate, fontSize = 14.sp, textAlign = TextAlign.Center
        )

        if (album.genres.isNotEmpty()) {
            Text(
                text = "Genres: ${album.genres.joinToString(", ")}",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center

            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        SpotifyLinkButton(externalURL = album.externalUrl)

    }

}

