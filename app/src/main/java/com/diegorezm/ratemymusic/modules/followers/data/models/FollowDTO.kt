package com.diegorezm.ratemymusic.modules.followers.data.models

import com.diegorezm.ratemymusic.modules.followers.domain.models.Follower
import com.diegorezm.ratemymusic.modules.followers.domain.models.Following
import com.google.firebase.Timestamp

data class FollowDTO(
    val followerId: String, // Who is following
    val followedId: String, // Who is being followed
    val createdAt: Timestamp = Timestamp.now()
)

fun FollowDTO.toFollower(): Follower {
    return Follower(
        profileId = this.followerId,
        createdAt = this.createdAt
    )
}

fun FollowDTO.toFollowing(): Following {
    return Following(
        profileId = this.followedId,
        createdAt = this.createdAt
    )
}