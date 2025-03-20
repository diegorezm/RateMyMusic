package com.diegorezm.ratemymusic.modules.reviews.data.repositories

import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewDTO
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewFilter
import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review

interface ReviewsRepository {
    suspend fun create(review: ReviewDTO): Result<String>
    suspend fun edit(reviewId: String, review: ReviewDTO): Result<Unit>
    suspend fun remove(reviewId: String): Result<Unit>
    suspend fun getReviews(filter: ReviewFilter): Result<List<Review>>
    suspend fun getReview(reviewId: String): Result<Review?>
}