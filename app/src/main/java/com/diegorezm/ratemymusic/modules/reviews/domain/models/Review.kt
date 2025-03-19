package com.diegorezm.ratemymusic.modules.reviews.domain.models

import com.diegorezm.ratemymusic.modules.reviews.data.models.EntityType
import com.google.firebase.Timestamp

data class Review(
    val reviwerId: String,
    val entityId: String,

    val entityType: EntityType,

    val reviwerName: String,
    val reviwerPhotoUrl: String,

    val content: String,

    val timestamp: Timestamp
)
