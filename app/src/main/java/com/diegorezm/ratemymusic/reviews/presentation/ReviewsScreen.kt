package com.diegorezm.ratemymusic.reviews.presentation

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.app.Route
import com.diegorezm.ratemymusic.core.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.core.presentation.toUiText
import com.diegorezm.ratemymusic.reviews.presentation.components.ReviewForm
import com.diegorezm.ratemymusic.reviews.presentation.components.ReviewList

@Composable
fun ReviewsScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: ReviewViewModel,
    navController: NavController,
    showForm: Boolean = false
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ReviewsScreen(modifier.padding(16.dp), state, onAction = {
        when (it) {
            is ReviewsScreenActions.OnProfileClick -> {
                val route = Route.Profile(it.profileId)
                navController.navigate(route)
            }

            else -> viewModel.onAction(it)
        }

    }, showForm)
}

@Composable
private fun ReviewsScreen(
    modifier: Modifier = Modifier,
    state: ReviewsScreenState,
    onAction: (ReviewsScreenActions) -> Unit,
    showForm: Boolean = false
) {
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        if (showForm) {
            ReviewForm(
                onSubmit = { content, rating ->
                    onAction(ReviewsScreenActions.OnCreateReview(content, rating))
                }
            )
            Spacer(modifier = Modifier.height(22.dp))
        }

        if (state.error != null) {
            Text(
                text = state.error.toUiText().asString(),
                style = MaterialTheme.typography.titleSmall
            )
        }

        if (state.reviews.isNotEmpty()) {
            ReviewList(
                reviews = state.reviews, currentUserId = state.currentUserId ?: "",
                onAction = onAction
            )
        } else {
            Text(
                text = stringResource(id = R.string.nothing_to_see_here),
                style = MaterialTheme.typography.titleSmall
            )
        }

        if (state.isLoading) {
            LoadingIndicator()
        }
    }
}

@Preview(name = "ReviewsScreen")
@Composable
private fun PreviewReviewsScreen() {

    ReviewsScreen(
        state = ReviewsScreenState(),
        onAction = {},
        showForm = true
    )
}