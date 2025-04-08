package com.diegorezm.ratemymusic.music.tracks.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegorezm.ratemymusic.music.tracks.domain.Track
import com.diegorezm.ratemymusic.music.tracks.presentation.TrackScreenActions

@Composable
fun TrackScreenContent(
    track: Track,
    onAction: (TrackScreenActions) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            TrackDetails(track = track)
        }
        item {
            TrackActions(
                isFavorite = false,
                onAction = onAction
            )
        }
    }
}