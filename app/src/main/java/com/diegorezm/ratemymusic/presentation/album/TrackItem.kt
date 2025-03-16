package com.diegorezm.ratemymusic.presentation.album

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diegorezm.ratemymusic.modules.music.domain.models.Track

@Composable
fun TrackItem(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = track.name,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = formatDuration(track.duration),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}


fun formatDuration(durationMs: Int): String {
    val minutes = durationMs / 60000
    val seconds = (durationMs % 60000) / 1000
    return "%02d:%02d".format(minutes, seconds)
}
