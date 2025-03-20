package com.diegorezm.ratemymusic.modules.reviews.domain.use_cases

import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository
import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review

suspend fun getReviewUseCase(
    reviewId: String,
    reviewsRepository: ReviewsRepository
): Result<Review?> {
    return reviewsRepository.getReview(reviewId)
}