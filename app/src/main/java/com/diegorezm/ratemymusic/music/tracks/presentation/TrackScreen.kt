package com.diegorezm.ratemymusic.music.tracks.presentation

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
import com.diegorezm.ratemymusic.music.tracks.presentation.components.TrackScreenContent
import com.diegorezm.ratemymusic.reviews.presentation.ReviewViewModel
import com.diegorezm.ratemymusic.reviews.presentation.ReviewsScreenRoot
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrackScreenRoot(
    viewModel: TrackScreenViewModel = koinViewModel(),
    reviewsViewModel: ReviewViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TrackScreen(state = state, onAction = {
        when (it) {
            is TrackScreenActions.OnBackClick -> {
                navController.navigateUp()
            }

            else -> viewModel.onAction(it)
        }
    })

    BottomDrawer(state.openReviewDialog, onDismiss = {
        viewModel.onAction(TrackScreenActions.OnCloseReviewsDrawer)
    }) {
        ReviewsScreenRoot(
            viewModel = reviewsViewModel,
            navController = navController,
            showForm = true
        )
    }
}

@Composable
private fun TrackScreen(state: TrackScreenState, onAction: (TrackScreenActions) -> Unit) {
    ScaffoldWithTopBar(
        title = stringResource(R.string.track_details),
        onBackClick = {
            onAction(TrackScreenActions.OnBackClick)
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

        if (state.track != null) {
            TrackScreenContent(track = state.track, isFavorite = state.isFavorite, onAction)
        }
    }
}

@Composable
@Preview(showBackground = true, name = "TrackScreen")
private fun TrackScreenPreview() {
    TrackScreen(state = TrackScreenState(), onAction = {})
}