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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.modules.music.domain.models.Artist
import com.diegorezm.ratemymusic.presentation.components.ThemedCard

@Composable
fun ArtistList(artists: List<Artist>) {
    LazyColumn {
        items(artists) { artist ->
            AristItem(artist)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun AristItem(artist: Artist) {
    ThemedCard {
        Row {
            AsyncImage(
                model = artist.imageURL,
                contentDescription = "Arist ${artist.name} picture",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .height(60.dp)
                    .width(60.dp)
            )
            Column(
                Modifier.weight(1f)
            ) {
                Text(text = artist.name, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = "${artist.genres.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium
                )

            }
        }
    }
}