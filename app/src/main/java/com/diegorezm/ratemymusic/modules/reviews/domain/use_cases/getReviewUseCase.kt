package com.diegorezm.ratemymusic.modules.reviews.domain.use_cases

import android.util.Log
import com.diegorezm.ratemymusic.modules.common.PublicException
import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository
import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review

suspend fun getReviewUseCase(
    reviewId: String,
    reviewsRepository: ReviewsRepository
): Result<Review> {
    return try {
        val result = reviewsRepository.getReview(reviewId)
        Result.success(result)
    } catch (e: PublicException) {
        Result.failure(e)
    } catch (e: Exception) {
        Log.e("GetReviewUseCase", "Error getting review: ${e.message}", e)
        Result.failure(Exception("Error getting review."))
    }
}