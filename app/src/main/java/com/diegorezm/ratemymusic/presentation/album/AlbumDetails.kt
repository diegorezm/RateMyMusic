package com.diegorezm.ratemymusic.presentation.album

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.modules.music.domain.models.Album
import java.util.Locale

@Composable
fun AlbumDetail(album: Album) {
    val formattedDate = formatReleaseDate(album.releaseDate)
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Album Cover
        AsyncImage(
            model = album.imageURL,
            contentDescription = "Album Cover",
            modifier = Modifier
                .size(400.dp)
                .clip(shape = MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Album Name
        Text(
            text = album.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        // Album Label
        Text(
            text = album.label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary
        )

        // Release Date
        Text(text = formattedDate, fontSize = 14.sp)
        Text(text = "Total tracks: ${album.totalTracks}", fontSize = 14.sp)

        // Genres
        if (album.genres.isNotEmpty()) {
            Text(
                text = "Genres: ${album.genres.joinToString(", ")}",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, album.externalUrl.toUri())
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.spotify_logo),
                    contentDescription = "Spotify Logo",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Listen on Spotify", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    Log.e("AlbumDetail", "Like button clicked")
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Like",
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(onClick = {
                Log.e("AlbumDetail", "Comment button clicked")
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Comment",
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tracks List
        Column {
            album.tracks.items.forEach { track ->
                TrackItem(track)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


fun formatReleaseDate(dateString: String): String {
    return try {
        val originalFormat =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val targetFormat = SimpleDateFormat(
            "MMMM dd, yyyy",
            Locale.getDefault()
        )
        val date = originalFormat.parse(dateString)
        date?.let { targetFormat.format(it) }
            ?: dateString
    } catch (e: Exception) {
        Log.e("AlbumComponent", "Error formatting release date: $dateString", e)
        dateString
    }
}