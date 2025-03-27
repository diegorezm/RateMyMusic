package com.diegorezm.ratemymusic.presentation.album

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegorezm.ratemymusic.modules.music.domain.models.TrackSimple
import com.diegorezm.ratemymusic.ui.theme.RateMyMusicTheme

@Composable
fun TrackItem(track: TrackSimple, onClick: (String) -> Unit) {
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
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = formatDuration(track.duration),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
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
    val trackItemSimple = TrackSimple(id = "a", name = "b", duration = 500)
    RateMyMusicTheme {
        TrackItem(trackItemSimple, onClick = {})
    }
}