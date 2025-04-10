package com.diegorezm.ratemymusic.reviews.domain

import com.diegorezm.ratemymusic.profile.domain.models.Profile
import kotlinx.datetime.Instant

data class Review(
    val id: String,
    val content: String,
    val entityId: String,
    val entity: String,
    val rating: Int,
    val profile: Profile,
    val createdAt: Instant
)
