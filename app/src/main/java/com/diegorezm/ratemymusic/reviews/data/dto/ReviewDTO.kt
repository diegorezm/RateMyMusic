package com.diegorezm.ratemymusic.reviews.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewDTO(
    @SerialName("reviewer_id") val reviewerId: String,
    @SerialName("entity_id") val entityId: String,
    @SerialName("entity_type") val reviewType: ReviewEntityDTO,
    val content: String,
    val rating: Int = 1,
)
