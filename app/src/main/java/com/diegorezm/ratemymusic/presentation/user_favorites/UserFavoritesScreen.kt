package com.diegorezm.ratemymusic.presentation.user_favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.AlbumRouteId
import com.diegorezm.ratemymusic.TrackRouteId
import com.diegorezm.ratemymusic.presentation.components.CarouselItem
import com.diegorezm.ratemymusic.presentation.components.ErrorMessage
import com.diegorezm.ratemymusic.presentation.components.HorizontalCarousel
import com.diegorezm.ratemymusic.presentation.components.LoadingIndicator

@Composable
fun UserFavoritesScreen(
    navController: NavController,
    userFavoritesViewModel: UserFavoritesViewModel
) {
    val userFavoritesState by userFavoritesViewModel.userFavoritesState.collectAsState()

    val albumsState by userFavoritesViewModel.albumsState.collectAsState()
    val tracksState by userFavoritesViewModel.tracksState.collectAsState()
    // val artists by userFavoritesViewModel.artistsState.collectAsState()

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        when (userFavoritesState) {
            is UserFavoritesState.Success -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    AlbumsState(albumsState, navController)
                    Spacer(modifier = Modifier.height(16.dp))
                    TracksState(tracksState, navController)
                }
            }

            is UserFavoritesState.Error -> {
                val err = (userFavoritesState as UserFavoritesState.Error).message
                Text(err)
            }

            is UserFavoritesState.Idle -> LoadingIndicator()
            is UserFavoritesState.Loading -> LoadingIndicator()
        }
    }
}

@Composable
private fun TracksState(tracksState: FavoriteTracksState, navController: NavController) {
    when (tracksState) {
        is FavoriteTracksState.Error -> {
            val err = tracksState.message
            ErrorMessage(err)
        }

        FavoriteTracksState.Idle -> return

        FavoriteTracksState.Loading -> LoadingIndicator()
        is FavoriteTracksState.Success -> {
            val tracks = tracksState.tracks
            if (tracks.isEmpty()) return
            val carouselItems = tracks.map {
                CarouselItem(
                    id = it.id,
                    name = it.name,
                    imageUrl = it.albumCoverURL,
                    description = it.artists.joinToString(", ") { it.name }
                )
            }

            Text(text = "Favorite tracks", style = MaterialTheme.typography.titleMedium)
            HorizontalCarousel(carouselItems) {
                val routeId = TrackRouteId(
                    it
                )
                navController.navigate(routeId)
            }
        }
    }

}

@Composable
private fun AlbumsState(albumsState: FavoriteAlbumsState, navController: NavController) {
    when (albumsState) {
        is FavoriteAlbumsState.Success -> {
            val albums = albumsState.albums

            if (albums.isEmpty()) return

            val carouselItems = albums.map {
                CarouselItem(
                    id = it.id,
                    name = it.name,
                    imageUrl = it.imageURL ?: "",
                    description = it.artists.joinToString(", ") { it.name }
                )
            }

            Text(text = "Favorite albums", style = MaterialTheme.typography.titleMedium)
            HorizontalCarousel(carouselItems) {
                val routeId = AlbumRouteId(
                    it
                )
                navController.navigate(routeId)
            }
        }

        is FavoriteAlbumsState.Error -> {
            val err = albumsState.message
            Text(err)
        }

        FavoriteAlbumsState.Idle -> return

        FavoriteAlbumsState.Loading -> LoadingIndicator()
    }
}