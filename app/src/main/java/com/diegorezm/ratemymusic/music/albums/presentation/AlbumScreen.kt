package com.diegorezm.ratemymusic.music.albums.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.app.Route
import com.diegorezm.ratemymusic.core.presentation.components.BottomDrawer
import com.diegorezm.ratemymusic.core.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.core.presentation.components.ScaffoldWithTopBar
import com.diegorezm.ratemymusic.core.presentation.toUiText
import com.diegorezm.ratemymusic.music.albums.presentation.components.AlbumScreenContent
import com.diegorezm.ratemymusic.reviews.presentation.ReviewViewModel
import com.diegorezm.ratemymusic.reviews.presentation.ReviewsScreenRoot
import org.koin.androidx.compose.koinViewModel

@Composable
fun AlbumScreenRoot(
    viewModel: AlbumViewModel = koinViewModel(),
    reviewsViewModel: ReviewViewModel,
    navController: NavController,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AlbumScreen(state = state, onAction = { action ->
        when (action) {
            is AlbumScreenActions.OnBackClick -> {
                navController.navigateUp()
            }

            is AlbumScreenActions.OnTrackClick -> {
                val route = Route.TrackDetails(trackId = action.trackId)
                navController.navigate(route)
            }

            else -> viewModel.onAction(action)
        }
    })

    BottomDrawer(state.openReviewDialog, onDismiss = {
        viewModel.onAction(AlbumScreenActions.OnCloseReviewsDrawer)
    }) {
        ReviewsScreenRoot(
            viewModel = reviewsViewModel,
            navController = navController,
            showForm = true
        )
    }


}

@Composable
private fun AlbumScreen(
    state: AlbumScreenState,
    onAction: (AlbumScreenActions) -> Unit

) {
    ScaffoldWithTopBar(
        title = stringResource(R.string.album_details),
        onBackClick = {
            onAction(AlbumScreenActions.OnBackClick)
        }
    ) {

        if (state.isLoading) {
            LoadingIndicator()
        }

        if (state.error != null) {
            Text(
                text = state.error.toUiText().asString(),
                style = MaterialTheme.typography.titleSmall
            )
        }

        if (state.album != null) {
            AlbumScreenContent(album = state.album, isFavorite = state.isFavorite, onAction)
        }

    }
}

@Composable
@Preview(showBackground = true, name = "AlbumScreen")
private fun AlbumScreenPreview() {
    AlbumScreen(state = AlbumScreenState(), onAction = {})
}