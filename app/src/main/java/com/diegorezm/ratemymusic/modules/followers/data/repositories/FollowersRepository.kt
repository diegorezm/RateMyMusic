package com.diegorezm.ratemymusic.modules.followers.data.repositories

import com.diegorezm.ratemymusic.modules.followers.data.models.FollowDTO

interface FollowersRepository {
    suspend fun follow(payload: FollowDTO): Result<Unit>
    suspend fun unfollow(payload: FollowDTO): Result<Unit>
    suspend fun isFollower(followingId: String, followerId: String): Result<Boolean>
    suspend fun getFollowersCount(userId: String): Result<Int>
    suspend fun getFollowingCount(userId: String): Result<Int>
    suspend fun getFollowers(userId: String): Result<List<String>>
    suspend fun getFollowing(userId: String): Result<List<String>>
}