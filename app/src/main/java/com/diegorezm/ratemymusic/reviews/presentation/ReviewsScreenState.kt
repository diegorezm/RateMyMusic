package com.diegorezm.ratemymusic.reviews.presentation

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.reviews.domain.Review

data class ReviewsScreenState(
    val isLoading: Boolean = false,
    val reviews: List<Review> = emptyList(),
    val error: DataError? = null,
    val currentUserId: String? = null
)
