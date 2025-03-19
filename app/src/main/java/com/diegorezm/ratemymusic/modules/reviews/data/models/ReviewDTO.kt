package com.diegorezm.ratemymusic.modules.reviews.data.models

import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review
import com.google.firebase.Timestamp

data class ReviewDTO(
    val reviewerId: String,
    val entityType: EntityType,
    val reviwerName: String,
    val reviwerPhotoUrl: String?,
    val entityId: String,
    val content: String,
    val timestamp: Timestamp = Timestamp.now()
)

enum class EntityType {
    TRACK,
    ALBUM
}

fun ReviewDTO.toDomain(): Review {
    return Review(
        content = content,
        reviwerId = reviewerId,
        entityId = entityId,
        entityType = entityType,
        reviwerName = reviwerName,
        reviwerPhotoUrl = reviwerPhotoUrl ?: "",
        timestamp = timestamp,
    )
}