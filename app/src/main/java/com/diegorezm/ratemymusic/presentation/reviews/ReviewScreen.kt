package com.diegorezm.ratemymusic.presentation.reviews

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.firebase.auth.FirebaseAuth

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

