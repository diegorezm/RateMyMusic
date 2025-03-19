package com.diegorezm.ratemymusic.modules.reviews.domain.use_cases

import com.diegorezm.ratemymusic.modules.reviews.data.models.EntityType
import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository

suspend fun removeReviewUseCase(
    entityId: String,
    entityType: EntityType,
    userId: String,
    reviewsRepository: ReviewsRepository
): Result<Unit> {
    return reviewsRepository.remove(entityId, entityType, userId)
}