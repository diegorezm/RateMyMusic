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
import com.diegorezm.ratemymusic.modules.music.domain.models.AlbumSimple
import com.diegorezm.ratemymusic.presentation.components.ThemedCard

@Composable
fun AlbumList(albums: List<AlbumSimple>, onNavClick: (String) -> Unit) {
    LazyColumn {
        items(albums) { album ->
            AlbumItem(album, onNavClick)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun AlbumItem(album: AlbumSimple, onNavClick: (String) -> Unit = {}) {
    val names = album.artists.joinToString(", ") { it.name }
    ThemedCard {
        TextButton(onClick = {
            onNavClick(album.id)
        }) {
            Row {
                AsyncImage(
                    model = album.imageURL,
                    contentDescription = "Cover for ${album.name}",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(60.dp)
                        .width(60.dp)
                )
                Column(
                    Modifier.weight(1f)
                ) {
                    Text(text = album.name, style = MaterialTheme.typography.titleSmall)
                    Text(
                        text = "By $names",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}