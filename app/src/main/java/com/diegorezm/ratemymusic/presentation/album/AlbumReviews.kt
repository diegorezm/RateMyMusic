package com.diegorezm.ratemymusic.presentation.album

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.diegorezm.ratemymusic.presentation.reviews.ReviewsScreen
import com.diegorezm.ratemymusic.presentation.reviews.ReviewsViewModel

@Composable
fun AlbumReviews(reviewsViewModel: ReviewsViewModel) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ReviewsScreen(viewModel = reviewsViewModel)
    }
}