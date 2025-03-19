package com.diegorezm.ratemymusic.modules.reviews.data.repositories

import com.diegorezm.ratemymusic.modules.reviews.data.models.EntityType
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewDTO
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewFilter
import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review

interface ReviewsRepository {
    suspend fun create(review: ReviewDTO): Result<Unit>
    suspend fun edit(review: ReviewDTO): Result<Unit>
    suspend fun remove(entityId: String, entityType: EntityType, userId: String): Result<Unit>
    suspend fun getReviews(filter: ReviewFilter): Result<List<Review>>
}