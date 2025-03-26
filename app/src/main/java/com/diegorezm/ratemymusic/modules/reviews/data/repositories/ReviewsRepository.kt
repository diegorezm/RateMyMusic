package com.diegorezm.ratemymusic.modules.reviews.data.repositories

import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewDTO
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewFilter
import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review
import com.diegorezm.ratemymusic.modules.reviews.domain.models.ReviewWithProfile

interface ReviewsRepository {
    suspend fun create(review: ReviewDTO): Result<String>
    suspend fun edit(reviewId: String, review: ReviewDTO): Result<Unit>
    suspend fun remove(reviewId: String): Result<Unit>
    suspend fun getReviews(filter: ReviewFilter): Result<List<ReviewWithProfile>>
    suspend fun getReview(reviewId: String): Result<Review?>
}