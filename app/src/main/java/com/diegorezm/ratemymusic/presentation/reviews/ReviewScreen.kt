package com.diegorezm.ratemymusic.presentation.reviews

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ReviewsScreen(viewModel: ReviewsViewModel) {
    val reviews by viewModel.reviewsState.collectAsState()

    when (reviews) {
        is ReviewsState.Idle -> CircularProgressIndicator()
        is ReviewsState.Loading -> CircularProgressIndicator()
        is ReviewsState.Success -> {
            ReviewList((reviews as ReviewsState.Success).reviews)
        }

        is ReviewsState.Error -> {
            val message = (reviews as ReviewsState.Error).message
            Text(text = message)
        }
    }
}