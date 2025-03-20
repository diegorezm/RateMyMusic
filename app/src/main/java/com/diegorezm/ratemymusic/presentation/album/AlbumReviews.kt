package com.diegorezm.ratemymusic.presentation.album

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegorezm.ratemymusic.presentation.reviews.ReviewsScreen
import com.diegorezm.ratemymusic.presentation.reviews.ReviewsViewModel

@Composable
fun AlbumReviews(albumId: String, viewModel: AlbumViewModel, reviewsViewModel: ReviewsViewModel) {
    var newReview by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            OutlinedTextField(
                value = newReview,
                onValueChange = { newReview = it },
                label = { Text("New Review") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (newReview.isNotBlank()) {
                        viewModel.writeReview(newReview, albumId, reviewsViewModel::loadReviews)
                        newReview = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Submit")
            }

            Spacer(modifier = Modifier.height(22.dp))

            ReviewsScreen(reviewsViewModel)
        }
    }
}