package com.diegorezm.ratemymusic.music.artists.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegorezm.ratemymusic.music.artists.domain.Artist
import com.diegorezm.ratemymusic.music.artists.presentation.ArtistScreenActions

@Composable
fun ArtistScreenContent(
    artist: Artist,
    isFavorite: Boolean,
    onAction: (ArtistScreenActions) -> Unit = {},
) {
    LazyColumn {
        item {
            ArtistDetails(artist = artist)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            ArtistActions(isFavorite, onAction)
        }
    }
}