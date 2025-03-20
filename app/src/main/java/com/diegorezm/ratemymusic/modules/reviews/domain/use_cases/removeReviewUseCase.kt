package com.diegorezm.ratemymusic.modules.reviews.domain.use_cases

import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository

suspend fun removeReviewUseCase(
    reviewId: String,
    reviewsRepository: ReviewsRepository
): Result<Unit> {
    return reviewsRepository.remove(reviewId)
}