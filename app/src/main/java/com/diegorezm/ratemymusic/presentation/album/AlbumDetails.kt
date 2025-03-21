package com.diegorezm.ratemymusic.presentation.album

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.modules.music.domain.models.Album
import com.diegorezm.ratemymusic.presentation.components.SpotifyButton
import com.diegorezm.ratemymusic.presentation.reviews.ReviewsViewModel
import com.diegorezm.ratemymusic.utils.formatTimestamp

@Composable
fun AlbumDetail(
    navController: NavController,
    album: Album,
    viewModel: AlbumViewModel,
    reviewsViewModel: ReviewsViewModel
) {
    val formattedDate = formatTimestamp(album.releaseDate)
    val isFavorite by viewModel.isFavorite.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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

        Text(
            text = album.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(text = formattedDate, fontSize = 14.sp)

        if (album.genres.isNotEmpty()) {
            Text(
                text = "Genres: ${album.genres.joinToString(", ")}",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    if (!isFavorite) {
                        viewModel.addToFavorite()
                    } else {
                        viewModel.removeFromFavorites()
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Like",
                    modifier = Modifier.size(30.dp),
                    tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        SpotifyButton(externalURL = album.externalUrl)

        Spacer(modifier = Modifier.height(26.dp))

        AlbumTabs(album, navController, viewModel, reviewsViewModel)
    }
}

@Composable
private fun AlbumTabs(
    album: Album,
    navController: NavController,
    viewModel: AlbumViewModel,
    reviewsViewModel: ReviewsViewModel
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Tracks", "Reviews")


    Surface(
        color = MaterialTheme.colorScheme.primary,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title) },
                    selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f)
                )
            }
        }
    }


    Spacer(modifier = Modifier.height(16.dp))

    when (selectedTabIndex) {
        0 -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Total tracks: ${album.totalTracks}", fontSize = 14.sp)

                Spacer(modifier = Modifier.height(6.dp))

                album.tracks.items.forEach { track ->
                    TrackItem(track, viewModel, navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        1 -> {
            AlbumReviews(album.id, viewModel, reviewsViewModel)
        }
    }
}