package com.diegorezm.ratemymusic.presentation.track

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.modules.music.domain.models.Track

@Composable
fun TrackDetails(track: Track) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = track.albumCoverURL,
            contentDescription = "Album Cover",
            modifier = Modifier
                .size(400.dp)
                .clip(shape = MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = track.name,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary
        )


        Text(
            text = "Album: ${track.albumName}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = "Artists: ${track.artists.joinToString(", ") { it.name }}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}