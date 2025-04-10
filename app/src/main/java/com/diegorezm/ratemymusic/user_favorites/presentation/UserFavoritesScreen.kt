package com.diegorezm.ratemymusic.user_favorites.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.app.Route
import com.diegorezm.ratemymusic.core.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.core.presentation.toUiText
import com.diegorezm.ratemymusic.user_favorites.presentation.components.UserFavoriteAlbums
import com.diegorezm.ratemymusic.user_favorites.presentation.components.UserFavoriteTracks

@Composable
fun UserFavoritesScreenRoot(
    viewModel: UserFavoritesViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    UserFavoritesScreen(modifier = Modifier.padding(16.dp), state = state, onAction = {
        when (it) {
            is UserFavoritesScreenActions.OnAlbumClicked -> {
                val route = Route.AlbumDetails(it.albumId)
                navController.navigate(route)
            }

            is UserFavoritesScreenActions.OnTrackClicked -> {
                val route = Route.TrackDetails(it.trackId)
                navController.navigate(route)
            }

            else -> viewModel.onAction(it)
        }
    })
}

@Composable
private fun UserFavoritesScreen(
    modifier: Modifier = Modifier,
    state: UserFavoritesState,
    onAction: (UserFavoritesScreenActions) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (state.albums.isNotEmpty()) {
            UserFavoriteAlbums(albums = state.albums, onAlbumClick = {
                onAction(UserFavoritesScreenActions.OnAlbumClicked(it))
            })
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (state.tracks.isNotEmpty()) {
            UserFavoriteTracks(tracks = state.tracks, onTrackClick = {
                onAction(UserFavoritesScreenActions.OnTrackClicked(it))
            })
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (state.error != null) {
            Text(
                text = state.error.toUiText().asString(),
                style = MaterialTheme.typography.titleSmall
            )
        }

        if (state.isLoading) {
            LoadingIndicator()
        }
    }
}