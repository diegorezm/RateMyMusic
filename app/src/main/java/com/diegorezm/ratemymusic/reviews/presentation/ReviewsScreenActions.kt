package com.diegorezm.ratemymusic.reviews.presentation

sealed interface ReviewsScreenActions {
    data class OnCreateReview(val content: String, val rating: Int) : ReviewsScreenActions
    data class OnEditReview(val reviewId: String, val content: String, val rating: Int) :
        ReviewsScreenActions

    data class OnDeleteReview(val reviewId: String) : ReviewsScreenActions
    data class OnProfileClick(val profileId: String) : ReviewsScreenActions
}