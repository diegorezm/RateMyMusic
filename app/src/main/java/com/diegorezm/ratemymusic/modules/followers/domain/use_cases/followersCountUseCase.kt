package com.diegorezm.ratemymusic.modules.followers.domain.use_cases

import android.util.Log
import com.diegorezm.ratemymusic.modules.common.PublicException
import com.diegorezm.ratemymusic.modules.followers.data.repositories.FollowersRepository

suspend fun getFollowersCountUseCase(
    userId: String,
    followersRepository: FollowersRepository
): Result<Int> {
    return try {
        val response = followersRepository.getFollowersCount(userId)
        Result.success(response)
    } catch (e: PublicException) {
        Result.failure(e)
    } catch (e: Exception) {
        Log.e("getFollowersCountUseCase", "Error getting followers count: ${e.message}", e)
        Result.failure(PublicException("Error getting followers count."))
    }
}