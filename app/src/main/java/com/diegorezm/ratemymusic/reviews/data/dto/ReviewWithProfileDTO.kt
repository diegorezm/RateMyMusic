package com.diegorezm.ratemymusic.reviews.data.dto

import com.diegorezm.ratemymusic.profile.data.dto.ProfileDTO
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ReviewWithProfileDTO(
    @SerialName("id") val id: String,
    @SerialName("reviewer_id") val reviewerId: String,
    @SerialName("entity_id") val entityId: String,
    @SerialName("entity_type") val entityType: ReviewEntityDTO,
    @SerialName("profiles") val profile: ProfileDTO,
    @SerialName("created_at") val createdAt: Instant,
    val content: String,
    val rating: Int
)
