package com.diegorezm.ratemymusic.modules.followers.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Followers(
    val id: String,
    val followerId: String,
    val followingId: String,
    val createdAt: String
)
