package com.diegorezm.ratemymusic.modules.reviews.domain.models

import com.diegorezm.ratemymusic.modules.reviews.data.models.EntityType
import com.google.firebase.Timestamp

data class Review(
    val id: String,
    val reviewerId: String,
    val entityId: String,
    val entityType: EntityType,
    val reviewerName: String,
    val reviewerPhotoUrl: String,
    val content: String,
    val createdAt: Timestamp,
) {
    constructor() : this("", "", "", EntityType.ALBUM, "", "", "", Timestamp.now())
}
