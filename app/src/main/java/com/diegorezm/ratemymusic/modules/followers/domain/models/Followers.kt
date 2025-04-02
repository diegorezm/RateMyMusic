package com.diegorezm.ratemymusic.modules.followers.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Followers(
    val id: String,
    @SerialName("follower_id") val followerId: String,
    @SerialName("following_id") val followingId: String,
    @SerialName("created_at") val createdAt: String
)
