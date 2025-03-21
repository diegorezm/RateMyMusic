package com.diegorezm.ratemymusic.presentation.user_favorites

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.AlbumRouteId
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.TrackRouteId
import com.diegorezm.ratemymusic.modules.music.domain.models.Album
import com.diegorezm.ratemymusic.modules.music.domain.models.Artist
import com.diegorezm.ratemymusic.modules.music.domain.models.Track
import com.diegorezm.ratemymusic.modules.reviews.data.models.EntityType

data class CarouselItem(
    val id: String,
    val name: String,
    val imageUrl: String,
    val type: EntityType
)

@Composable
fun FavoritesCarousel(
    tracks: List<Track> = emptyList(),
    albums: List<Album> = emptyList(),
    artists: List<Artist> = emptyList(),
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (tracks.isNotEmpty()) {
            val carouselItems = tracks.map { track ->
                CarouselItem(
                    id = track.id,
                    name = track.name,
                    imageUrl = track.albumCoverURL,
                    type = EntityType.TRACK
                )
            }
            Text("Favorite Tracks", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalCarousel(items = carouselItems, onClick = {
                val trackRouteId = TrackRouteId(it)
                navController.navigate(trackRouteId)
            })
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (albums.isNotEmpty()) {
            val carouselItems = albums.map { album ->
                CarouselItem(
                    id = album.id,
                    name = album.name,
                    imageUrl = album.imageURL ?: "",
                    type = EntityType.ALBUM
                )
            }
            Text("Favorite Albums", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalCarousel(items = carouselItems, onClick = {
                val albumRouteID = AlbumRouteId(it)
                navController.navigate(albumRouteID)
            })
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (artists.isNotEmpty()) {
            val carouselItems = artists.map { artist ->
                CarouselItem(
                    id = artist.id,
                    name = artist.name,
                    imageUrl = artist.imageURL,
                    type = EntityType.ARTIST
                )
            }
            Text("Favorite Artists", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalCarousel(items = carouselItems, onClick = {
                Log.d("FavouritesCarousel", "Artist clicked: $it")
            })
        }
    }
}

@Composable
fun HorizontalCarousel(items: List<CarouselItem>, onClick: (String) -> Unit = {}) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(items) { item ->
            FavoriteItemCard(item, onClick)
        }
    }
}

@Composable
fun FavoriteItemCard(item: CarouselItem, onClick: (String) -> Unit = {}) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(200.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                onClick(item.id)
            },
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = item.imageUrl,
                placeholder = painterResource(R.drawable.spotify_logo_black),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(135.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}