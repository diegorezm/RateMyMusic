package com.diegorezm.ratemymusic.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.modules.music.domain.models.Track
import com.diegorezm.ratemymusic.presentation.components.ThemedCard

@Composable
fun TrackList(tracks: List<Track>, onNavClick: (String) -> Unit) {
    LazyColumn {
        items(tracks) { track ->
            TrackItem(track, onNavClick)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TrackItem(track: Track, onNavClick: (String) -> Unit = {}) {
    val names = track.artists.joinToString(", ") { it.name }
    ThemedCard {
        TextButton(
            onClick = {
                onNavClick(track.id)
            }
        ) {

            Row {
                AsyncImage(
                    model = track.albumCoverURL,
                    contentDescription = "Cover for ${track.albumName}",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(60.dp)
                        .width(60.dp)
                )
                Column(
                    Modifier.weight(1f)
                ) {
                    Text(text = track.name, style = MaterialTheme.typography.titleSmall)
                    Text(
                        text = "By $names",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

    }
}