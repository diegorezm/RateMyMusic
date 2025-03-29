package com.diegorezm.ratemymusic.modules.reviews.data.repositories

import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewDTO
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewType
import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review
import com.diegorezm.ratemymusic.modules.reviews.domain.models.ReviewWithProfile

interface ReviewsRepository {
    suspend fun create(review: ReviewDTO)
    suspend fun edit(reviewId: String, review: ReviewDTO)
    suspend fun remove(reviewId: String)
    suspend fun getEntityReviews(entityId: String, type: ReviewType): List<ReviewWithProfile>
    suspend fun getUserReviews(userId: String): List<ReviewWithProfile>
    suspend fun getReview(reviewId: String): Review
}