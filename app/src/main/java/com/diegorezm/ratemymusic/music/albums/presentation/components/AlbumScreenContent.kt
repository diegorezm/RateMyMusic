package com.diegorezm.ratemymusic.music.albums.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegorezm.ratemymusic.music.albums.domain.Album
import com.diegorezm.ratemymusic.music.albums.presentation.AlbumScreenActions

@Composable
fun AlbumScreenContent(
    album: Album,
    onAction: (AlbumScreenActions) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            AlbumDetails(album = album)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            AlbumActions(
                isFavorite = false,
                onAction = onAction
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            AlbumTracks(
                album = album,
                onTrackClick = { onAction(AlbumScreenActions.OnTrackClick(it)) }
            )
        }
    }
}
