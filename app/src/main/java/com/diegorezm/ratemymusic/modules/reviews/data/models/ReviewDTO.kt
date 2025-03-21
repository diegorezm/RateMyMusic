package com.diegorezm.ratemymusic.modules.reviews.data.models

import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review
import com.google.firebase.Timestamp

data class ReviewDTO(
    val reviewerId: String,
    val entityType: EntityType,
    val reviewerName: String,
    val reviewerPhotoUrl: String?,
    val entityId: String,
    val content: String,
    val createdAt: Timestamp = Timestamp.now()
)

enum class EntityType {
    TRACK,
    ALBUM,
    ARTIST
}

fun ReviewDTO.toDomain(): Review {
    return Review(
        id = "",
        content = content,
        reviewerId = reviewerId,
        entityId = entityId,
        entityType = entityType,
        reviewerName = reviewerName,
        reviewerPhotoUrl = reviewerPhotoUrl ?: "",
        createdAt = createdAt,
    )
}