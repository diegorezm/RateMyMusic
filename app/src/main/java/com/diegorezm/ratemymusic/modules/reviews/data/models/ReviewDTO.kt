package com.diegorezm.ratemymusic.modules.reviews.data.models

import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ReviewDTO(
    val reviewerId: String,
    val entityId: String,
    val reviewType: ReviewType,
    val content: String,
    val rating: Int = 1,
    val createdAt: String = Date().toString()
)

fun ReviewDTO.toDomain(): Review {
    return Review(
        id = "",
        content = content,
        reviewerId = reviewerId,
        entityId = entityId,
        entityType = reviewType,
        createdAt = createdAt,
    )
}