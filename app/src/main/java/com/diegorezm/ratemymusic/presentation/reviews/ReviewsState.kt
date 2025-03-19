package com.diegorezm.ratemymusic.presentation.reviews

import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review

sealed class ReviewsState {
    object Idle : ReviewsState()
    object Loading : ReviewsState()
    data class Success(val reviews: List<Review>) : ReviewsState()
    data class Error(val message: String) : ReviewsState()
}