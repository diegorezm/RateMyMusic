package com.diegorezm.ratemymusic.music.artists.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.core.presentation.components.BottomDrawer
import com.diegorezm.ratemymusic.core.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.core.presentation.components.ScaffoldWithTopBar
import com.diegorezm.ratemymusic.core.presentation.toUiText
import com.diegorezm.ratemymusic.music.albums.presentation.AlbumScreenActions
import com.diegorezm.ratemymusic.music.artists.presentation.components.ArtistScreenContent
import com.diegorezm.ratemymusic.reviews.presentation.ReviewViewModel
import com.diegorezm.ratemymusic.reviews.presentation.ReviewsScreenRoot
import org.koin.androidx.compose.koinViewModel

@Composable
fun ArtistScreenRoot(
    viewModel: ArtistViewModel = koinViewModel(),
    reviewsViewModel: ReviewViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ArtistScreen(state = state, onAction = { action ->
        when (action) {
            is ArtistScreenActions.OnBackClick -> {
                navController.navigateUp()
            }

            else -> viewModel.onAction(action)
        }
    })

    BottomDrawer(state.openReviewDialog, onDismiss = {
        viewModel.onAction(ArtistScreenActions.OnCloseReviewsDrawer)
    }) {
        ReviewsScreenRoot(
            viewModel = reviewsViewModel,
            navController = navController,
            showForm = true
        )
    }
}

@Composable
private fun ArtistScreen(
    state: ArtistScreenState,
    onAction: (ArtistScreenActions) -> Unit = {},
) {
    ScaffoldWithTopBar(
        title = stringResource(R.string.artist_details),
        onBackClick = {
            onAction(ArtistScreenActions.OnBackClick)
        }
    ) {
        if (state.isLoading) {
            LoadingIndicator()
        }

        if (state.error != null) {
            Text(
                text = state.error.toUiText().asString(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (state.artist != null) {
            ArtistScreenContent(
                artist = state.artist,
                isFavorite = state.isFavorite,
                onAction = onAction
            )
        }

    }
}

@Preview(name = "ArtistScreen")
@Composable
private fun PreviewArtistScreen() {
    ArtistScreen(state = ArtistScreenState())
}