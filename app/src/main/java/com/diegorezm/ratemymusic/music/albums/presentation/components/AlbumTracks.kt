package com.diegorezm.ratemymusic.music.albums.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diegorezm.ratemymusic.core.presentation.theme.RateMyMusicTheme
import com.diegorezm.ratemymusic.music.albums.domain.Album
import com.diegorezm.ratemymusic.music.tracks.domain.Track


@Composable
fun AlbumTracks(album: Album, onTrackClick: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Total tracks: ${album.totalTracks}",
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            album.tracks.forEach { track ->
                TrackItem(track, onClick = {
                    onTrackClick(it)
                })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }
}


@Composable
private fun TrackItem(track: Track, onClick: (String) -> Unit) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        shape = MaterialTheme.shapes.medium,
        onClick = {
            onClick(track.id)
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(
                        weight = 1.0f,
                        fill = false,
                    ),
                text = track.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = formatDuration(track.duration),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.End
            )
        }
    }

}


fun formatDuration(durationMs: Int): String {
    val minutes = durationMs / 60000
    val seconds = (durationMs % 60000) / 1000
    return "%02d:%02d".format(minutes, seconds)
}

@Preview(showBackground = true)
@Composable
private fun TrackItemPreview() {
    val trackItemSimple = Track(
        id = "a", name = "b", duration = 500, artists = emptyList(),
        externalUrl = ""
    )

    RateMyMusicTheme {
        TrackItem(trackItemSimple, onClick = {})
    }
}
