package com.diegorezm.ratemymusic.presentation.track

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.modules.music.domain.models.Track
import com.diegorezm.ratemymusic.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.presentation.components.SpotifyButton
import com.diegorezm.ratemymusic.presentation.reviews.ReviewList
import com.diegorezm.ratemymusic.presentation.reviews.ReviewsState
import com.diegorezm.ratemymusic.presentation.reviews.ReviewsViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackScreen(
    navController: NavController,
    viewModel: TrackViewModel,
    reviewsViewModel: ReviewsViewModel
) {
    val track by viewModel.trackState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Track Details",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                navigationIcon = {
                    val imageVector =
                        ImageVector.vectorResource(R.drawable.baseline_keyboard_arrow_left_24)
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = imageVector, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (track) {
                is TrackState.Success -> {
                    val trackData = (track as TrackState.Success).track
                    if (trackData == null) {
                        NoTrackFound()
                    } else {
                        TrackContent(trackData, viewModel, reviewsViewModel)
                    }
                }

                is TrackState.Error -> ErrorMessage((track as TrackState.Error).message)
                TrackState.Idle, TrackState.Loading -> LoadingIndicator()
            }
        }
    }
}

@Composable
fun TrackContent(track: Track, trackViewModel: TrackViewModel, reviewsViewModel: ReviewsViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { AlbumCover(track.albumCoverURL) }
        item { TrackInfo(track) }
        item { FavoriteButton(trackViewModel) }
        item { SpotifyButton(track.externalUrl) }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item { TrackReviews(track, trackViewModel, reviewsViewModel) }
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
            contentScale = ContentScale.Crop
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
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Album: ${track.albumName}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Artists: ${track.artists.joinToString(", ") { it.name }}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
    Spacer(modifier = Modifier.height(16.dp))


}

@Composable
fun FavoriteButton(trackViewModel: TrackViewModel) {
    val isFavorite by trackViewModel.isFavorite.collectAsState()

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
                imageVector = Icons.Default.Favorite,
                contentDescription = "Like",
                modifier = Modifier.size(30.dp),
                tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun NoTrackFound() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "No track data available", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ErrorMessage(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun TrackReviews(track: Track, trackViewModel: TrackViewModel, reviewsViewModel: ReviewsViewModel) {
    var newReview by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = newReview,
            onValueChange = { newReview = it },
            label = { Text("New Review") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (newReview.isNotBlank()) {
                    trackViewModel.writeReview(
                        newReview,
                        track.id,
                        reviewsViewModel::loadReviews
                    )
                    newReview = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Submit")
        }

        Spacer(modifier = Modifier.height(22.dp))

        ReviewsScreen(reviewsViewModel)
    }
}

@Composable
fun ReviewsScreen(viewModel: ReviewsViewModel) {
    val reviews by viewModel.reviewsState.collectAsState()
    val user = FirebaseAuth.getInstance().currentUser

    if (user == null) {
        Text(text = "User not logged in")
        return
    }

    when (reviews) {
        is ReviewsState.Idle -> CircularProgressIndicator()
        is ReviewsState.Loading -> CircularProgressIndicator()
        is ReviewsState.Success -> {
            ReviewList((reviews as ReviewsState.Success).reviews, user, viewModel)
        }

        is ReviewsState.Error -> {
            val message = (reviews as ReviewsState.Error).message
            Text(text = message)
        }
    }
}
