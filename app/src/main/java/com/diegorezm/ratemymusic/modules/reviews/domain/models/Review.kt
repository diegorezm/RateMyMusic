package com.diegorezm.ratemymusic.modules.reviews.domain.models

import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewType
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val id: String,
    val entityType: ReviewType,
    @SerialName("reviewer_id") val reviewerId: String,
    @SerialName("entity_id") val entityId: String,
    @SerialName("entity_type") val reviewType: ReviewType,
    val content: String,
    val rating: Int = 1,
    val createdAt: Instant,
) {
    constructor() : this(
        "",
        ReviewType.TRACK,
        "",
        "",
        ReviewType.TRACK,
        "",
        1,
        Instant.fromEpochMilliseconds(0)
    )
}
