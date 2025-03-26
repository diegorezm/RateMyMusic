package com.diegorezm.ratemymusic.modules.reviews.domain.models

import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewEntityType
import com.google.firebase.Timestamp

data class Review(
    val id: String,
    val reviewerId: String,
    val entityId: String,
    val entityType: ReviewEntityType,
    val content: String,
    val rating: Int = 1,
    val createdAt: Timestamp,
) {
    constructor() : this("", "", "", ReviewEntityType.ALBUM, "", 1, Timestamp.now())
}
