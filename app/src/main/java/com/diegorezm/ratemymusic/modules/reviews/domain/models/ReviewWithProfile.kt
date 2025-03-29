package com.diegorezm.ratemymusic.modules.reviews.domain.models

import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewType

data class ReviewWithProfile(
    val reviewId: String,
    val reviewerId: String,
    val entityId: String,
    val entityType: ReviewType,
    val reviewerName: String,
    val reviewerPhotoUrl: String?,
    val content: String,
    val createdAt: String,
    val rating: Int
)
