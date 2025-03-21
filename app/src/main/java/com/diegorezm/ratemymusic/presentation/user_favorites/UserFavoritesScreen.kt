package com.diegorezm.ratemymusic.presentation.user_favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.presentation.components.LoadingIndicator

@Composable
fun UserFavoritesScreen(
    navController: NavController,
    userFavoritesViewModel: UserFavoritesViewModel
) {
    val userFavoritesState by userFavoritesViewModel.userFavoritesState.collectAsState()

    val albums by userFavoritesViewModel.albumsState.collectAsState()
    val artists by userFavoritesViewModel.artistsState.collectAsState()
    val tracks by userFavoritesViewModel.tracksState.collectAsState()

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        when (userFavoritesState) {
            is UserFavoritesState.Success -> {
                FavoritesCarousel(
                    albums = albums,
                    artists = artists,
                    tracks = tracks,
                    navController = navController
                )
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