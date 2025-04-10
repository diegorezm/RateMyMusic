package com.diegorezm.ratemymusic.reviews.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegorezm.ratemymusic.reviews.domain.Review
import com.diegorezm.ratemymusic.reviews.presentation.ReviewsScreenActions

@Composable
fun ReviewList(
    reviews: List<Review>,
    currentUserId: String,
    onAction: (ReviewsScreenActions) -> Unit
) {
    LazyColumn {
        items(reviews) { review ->
            ReviewListItem(review = review, currentUserId = currentUserId, onAction = onAction)
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}