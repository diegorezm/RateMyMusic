package com.diegorezm.ratemymusic.modules.followers.data.repositories

import com.diegorezm.ratemymusic.modules.followers.data.models.FollowDTO

interface FollowersRepository {
    suspend fun follow(payload: FollowDTO)
    suspend fun unfollow(
        payload: FollowDTO
    )

    suspend fun isFollower(payload: FollowDTO): Boolean
    suspend fun getFollowersCount(userId: String): Int
    suspend fun getFollowingCount(userId: String): Int
}