package com.diegorezm.ratemymusic.modules.followers.data.models

import kotlinx.serialization.Serializable

@Serializable
data class FollowDTO(
    val followerId: String, // Who is following
    val followingId: String, // Who is being followed
)