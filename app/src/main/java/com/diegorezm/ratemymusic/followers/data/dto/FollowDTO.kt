package com.diegorezm.ratemymusic.followers.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FollowDTO(
    @SerialName("follower_id") val followerId: String, // Who is following
    @SerialName("following_id") val followingId: String, // Who is being followed
)
