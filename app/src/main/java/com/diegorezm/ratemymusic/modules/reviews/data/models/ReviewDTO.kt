package com.diegorezm.ratemymusic.modules.reviews.data.models

import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review
import com.google.firebase.Timestamp

data class ReviewDTO(
    val reviewerId: String,
    val entityId: String,
    val entityType: ReviewEntityType,
    val content: String,
    val rating: Int = 1,
    val createdAt: Timestamp = Timestamp.now()
)

enum class ReviewEntityType {
    TRACK,
    ALBUM,
    ARTIST,
}

fun ReviewDTO.toDomain(): Review {
    return Review(
        id = "",
        content = content,
        reviewerId = reviewerId,
        entityId = entityId,
        entityType = entityType,
        createdAt = createdAt,
    )
}