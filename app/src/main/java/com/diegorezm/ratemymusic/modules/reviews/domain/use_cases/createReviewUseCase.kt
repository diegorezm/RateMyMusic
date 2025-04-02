package com.diegorezm.ratemymusic.modules.reviews.domain.use_cases

import android.util.Log
import com.diegorezm.ratemymusic.modules.common.PublicException
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewDTO
import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository

suspend fun createReviewUseCase(
    review: ReviewDTO,
    reviewsRepository: ReviewsRepository
): Result<Unit> {
    return try {
        Result.success(reviewsRepository.create(review))
    } catch (e: PublicException) {
        Result.failure(e)
    } catch (e: Exception) {
        Log.e("CreateReviewUseCase", "Error creating review: ${e.message}", e)
        Result.failure(PublicException("Error creating review."))
    }
}