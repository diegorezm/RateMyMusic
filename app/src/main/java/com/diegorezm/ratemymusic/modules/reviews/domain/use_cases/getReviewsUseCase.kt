package com.diegorezm.ratemymusic.modules.reviews.domain.use_cases

import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewFilter
import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository
import com.diegorezm.ratemymusic.modules.reviews.domain.models.ReviewWithProfile

suspend fun getReviewsUseCase(
    filter: ReviewFilter,
    reviewsRepository: ReviewsRepository
): Result<List<ReviewWithProfile>> {
    return reviewsRepository.getReviews(filter)
}