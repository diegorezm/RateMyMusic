package com.diegorezm.ratemymusic.presentation.album

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.modules.music.domain.models.Album
import com.diegorezm.ratemymusic.presentation.components.BottomDrawer
import com.diegorezm.ratemymusic.presentation.components.ErrorMessage
import com.diegorezm.ratemymusic.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.presentation.components.ScaffoldWithTopBar
import com.diegorezm.ratemymusic.presentation.reviews.ReviewsScreen
import com.diegorezm.ratemymusic.presentation.reviews.ReviewsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    navController: NavController,
    viewModel: AlbumViewModel,
    reviewsViewModel: ReviewsViewModel,
) {
    val albumState by viewModel.albumState.collectAsState()

    ScaffoldWithTopBar(title = "Album Details", navController = navController) {

        when (albumState) {
            is AlbumState.Error -> {
                ErrorMessage((albumState as AlbumState.Error).message)
            }

            AlbumState.Idle, AlbumState.Loading -> LoadingIndicator()

            is AlbumState.Success -> {
                val albumData = (albumState as AlbumState.Success).album
                if (albumData == null) {
                    Text(text = "No album data available")
                } else {
                    AlbumContent(
                        navController,
                        viewModel,
                        reviewsViewModel,
                        albumData
                    )
                }
            }
        }
    }
}

@Composable
private fun AlbumContent(
    navController: NavController,
    albumViewModel: AlbumViewModel,
    reviewsViewModel: ReviewsViewModel,
    album: Album
) {
    val isFavorite by albumViewModel.isFavorite.collectAsState()
    var openDrawer by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item(
        ) {
            AlbumInfo(
                album = album,
                isFavorite = isFavorite,
                addToFavorites = {
                    albumViewModel.addToFavorite()
                },
                removeFromFavorites = {
                    albumViewModel.removeFromFavorites()
                },
                onOpenDrawer = {
                    openDrawer = true
                }
            )
        }
        item {
            AlbumTracks(
                album = album,
                navController = navController
            )
        }
    }
    BottomDrawer(openDrawer, onDismiss = { openDrawer = false }) {
        ReviewsScreen(
            showForm = true,
            viewModel = reviewsViewModel,
            navController = navController
        )
    }
}
