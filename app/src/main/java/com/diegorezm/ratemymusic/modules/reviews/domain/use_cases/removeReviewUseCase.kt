package com.diegorezm.ratemymusic.modules.reviews.domain.use_cases

import android.util.Log
import com.diegorezm.ratemymusic.modules.common.PublicException
import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository

suspend fun removeReviewUseCase(
    reviewId: String,
    reviewsRepository: ReviewsRepository
): Result<Unit> {
    return try {
        Result.success(reviewsRepository.remove(reviewId))
    } catch (e: PublicException) {
        Result.failure(e)
    } catch (e: Exception) {
        Log.e("RemoveReviewUseCase", "Error removing review: ${e.message}", e)
        Result.failure(Exception("Error removing review."))
    }
}