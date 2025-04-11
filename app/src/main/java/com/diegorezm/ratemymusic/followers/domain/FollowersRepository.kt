package com.diegorezm.ratemymusic.followers.domain

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.EmptyResult
import com.diegorezm.ratemymusic.core.domain.Result
import kotlinx.coroutines.flow.Flow

interface FollowersRepository {
    suspend fun follow(follower: String, following: String): EmptyResult<DataError>
    suspend fun unfollow(
        follower: String, following: String
    ): EmptyResult<DataError>

    suspend fun isFollower(follower: String, following: String): Flow<Boolean>
    suspend fun getFollowersCount(userId: String): Result<Int, DataError>
    suspend fun getFollowingCount(userId: String): Result<Int, DataError>
}