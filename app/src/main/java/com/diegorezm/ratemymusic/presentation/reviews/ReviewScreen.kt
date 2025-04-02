package com.diegorezm.ratemymusic.presentation.reviews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.ProfileRouteId
import com.diegorezm.ratemymusic.SignInRouteId
import com.diegorezm.ratemymusic.presentation.components.ErrorMessage
import com.diegorezm.ratemymusic.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.presentation.components.Separator

@Composable
fun ReviewsScreen(
    showForm: Boolean = true,
    viewModel: ReviewsViewModel,
    navController: NavController
) {
    val reviews by viewModel.reviewsState.collectAsState()
    val currentUserId = viewModel.currentUserId

    if (currentUserId == null) {
        navController.navigate(SignInRouteId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showForm) {
            ReviewForm { newReview, stars ->
                viewModel.createReview(newReview, stars)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Separator()

            Spacer(modifier = Modifier.height(16.dp))
        }


        when (reviews) {
            is ReviewsState.Idle -> LoadingIndicator()
            is ReviewsState.Loading -> LoadingIndicator()
            is ReviewsState.Success -> {
                ReviewList(
                    (reviews as ReviewsState.Success).reviews,
                    currentUserId = currentUserId.toString(),
                    onClick = {
                        val routeId = ProfileRouteId(it)
                        navController.navigate(routeId)
                    },
                    onDelete = {
                        viewModel.removeReview(it)
                    })
            }

            is ReviewsState.Error -> {
                val message = (reviews as ReviewsState.Error).message
                ErrorMessage(message)
            }
        }
    }
}

