package com.diegorezm.ratemymusic.presentation.track

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.modules.music.domain.models.Track
import com.diegorezm.ratemymusic.presentation.components.BottomDrawer
import com.diegorezm.ratemymusic.presentation.components.ErrorMessage
import com.diegorezm.ratemymusic.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.presentation.components.ScaffoldWithTopBar
import com.diegorezm.ratemymusic.presentation.components.SpotifyButton
import com.diegorezm.ratemymusic.presentation.reviews.ReviewsScreen
import com.diegorezm.ratemymusic.presentation.reviews.ReviewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackScreen(
    navController: NavController,
    viewModel: TrackViewModel,
    reviewsViewModel: ReviewsViewModel
) {
    val track by viewModel.trackState.collectAsState()
    ScaffoldWithTopBar(title = "Track Details", navController = navController) {
        when (track) {
            is TrackState.Success -> {
                val trackData = (track as TrackState.Success).track
                if (trackData == null) {
                    NoTrackFound()
                } else {
                    TrackContent(trackData, viewModel, reviewsViewModel, navController)
                }
            }

            is TrackState.Error -> ErrorMessage((track as TrackState.Error).message)
            TrackState.Idle, TrackState.Loading -> LoadingIndicator()
        }
    }
}

@Composable
fun TrackContent(
    track: Track,
    trackViewModel: TrackViewModel,
    reviewsViewModel: ReviewsViewModel,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { AlbumCover(track.albumCoverURL) }
        item { TrackInfo(track) }
        item { ActionButtons(trackViewModel, reviewsViewModel, navController) }
        item { SpotifyButton(track.externalUrl) }
        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
fun AlbumCover(imageUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Album Cover",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun TrackInfo(track: Track) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = track.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = track.artists.joinToString(", ") { it.name },
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center

        )

        // TODO: Add album here somewhere
    }
    Spacer(modifier = Modifier.height(16.dp))


}

@Composable
fun ActionButtons(
    trackViewModel: TrackViewModel,
    reviewsViewModel: ReviewsViewModel,
    navController: NavController
) {
    val isFavorite by trackViewModel.isFavorite.collectAsState()
    var openDrawer by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                if (isFavorite) {
                    trackViewModel.removeFromFavorites()
                } else {
                    trackViewModel.addToFavorite()
                }
            },
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.Favorite,
                contentDescription = "Like",
                modifier = Modifier.size(30.dp),
                tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }

        IconButton(
            onClick = {
                openDrawer = true
            },
        ) {
            Image(
                painter = painterResource(R.drawable.ic_chat_24),
                contentDescription = "See reviews",
                modifier = Modifier.size(30.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    BottomDrawer(
        openDrawer,
        { openDrawer = false },
    ) {
        ReviewsScreen(viewModel = reviewsViewModel, navController = navController)
    }
}


@Composable
fun NoTrackFound() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "No track data available", style = MaterialTheme.typography.bodyLarge)
    }
}

