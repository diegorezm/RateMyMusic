package com.diegorezm.ratemymusic.modules.followers.domain.use_cases

import android.util.Log
import com.diegorezm.ratemymusic.modules.common.PublicException
import com.diegorezm.ratemymusic.modules.followers.data.repositories.FollowersRepository

suspend fun getFollowingCountUseCase(
    userId: String,
    followersRepository: FollowersRepository
): Result<Int> {
    return try {
        val response = followersRepository.getFollowingCount(userId)
        Result.success(response)
    } catch (e: PublicException) {
        Result.failure(e)
    } catch (e: Exception) {
        Log.e("getFollowingCountUseCase", "Error getting following count: ${e.message}", e)
        Result.failure(PublicException("Error getting following count."))
    }
}