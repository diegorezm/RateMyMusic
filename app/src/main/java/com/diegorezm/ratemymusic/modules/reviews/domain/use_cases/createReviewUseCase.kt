package com.diegorezm.ratemymusic.modules.reviews.domain.use_cases

import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewDTO
import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository

suspend fun createReviewUseCase(
    review: ReviewDTO,
    reviewsRepository: ReviewsRepository
): Result<Unit> {
    return reviewsRepository.create(review)
}