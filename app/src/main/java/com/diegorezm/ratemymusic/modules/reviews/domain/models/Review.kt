package com.diegorezm.ratemymusic.modules.reviews.domain.models

import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewType
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Review(
    val id: String,
    val reviewerId: String,
    val entityId: String,
    val entityType: ReviewType,
    val content: String,
    val rating: Int = 1,
    val createdAt: String,
) {
    constructor() : this("", "", "", ReviewType.ALBUM, "", 1, Date().toString())
}
