package com.diegorezm.ratemymusic.modules.reviews.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ReviewDTO(
    @SerialName("reviewer_id") val reviewerId: String,
    @SerialName("entity_id") val entityId: String,
    @SerialName("entity_type") val reviewType: ReviewType,
    val content: String,
    val rating: Int = 1,
    @SerialName("created_at") val createdAt: String = Date().toString()
)