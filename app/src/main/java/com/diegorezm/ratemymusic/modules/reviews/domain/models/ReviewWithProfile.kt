package com.diegorezm.ratemymusic.modules.reviews.domain.models

import com.diegorezm.ratemymusic.modules.profiles.domain.models.Profile
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewWithProfile(
    @SerialName("id") val id: String,
    @SerialName("reviewer_id") val reviewerId: String,
    @SerialName("entity_id") val entityId: String,
    @SerialName("entity_type") val entityType: ReviewType,
    @SerialName("profiles") val profile: Profile,
    @SerialName("created_at") val createdAt: String,
    val content: String,
    val rating: Int
)
