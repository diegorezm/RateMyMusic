package com.diegorezm.ratemymusic.modules.reviews.domain.use_cases

import android.util.Log
import com.diegorezm.ratemymusic.modules.common.PublicException
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewFilter
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewType
import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository
import com.diegorezm.ratemymusic.modules.reviews.domain.models.ReviewWithProfile

suspend fun getReviewsUseCase(
    filter: ReviewFilter,
    reviewsRepository: ReviewsRepository
): Result<List<ReviewWithProfile>> {
    return try {
        val res = when (filter) {
            is ReviewFilter.ByAlbum -> reviewsRepository.getEntityReviews(
                filter.albumId,
                ReviewType.ALBUM
            )

            is ReviewFilter.ByTrack -> reviewsRepository.getEntityReviews(
                filter.trackId,
                ReviewType.TRACK
            )

            is ReviewFilter.ByUser -> reviewsRepository.getUserReviews(filter.userId)
        }

        Result.success(res)
    } catch (e: PublicException) {
        Result.failure(e)
    } catch (e: Exception) {
        Log.e("GetReviewsUseCase", "Error getting reviews: ${e.message}", e)
        Result.failure(Exception("Error getting reviews."))
    }
}