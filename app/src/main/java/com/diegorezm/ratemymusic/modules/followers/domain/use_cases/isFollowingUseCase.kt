package com.diegorezm.ratemymusic.modules.followers.domain.use_cases

import android.util.Log
import com.diegorezm.ratemymusic.modules.followers.data.models.FollowDTO
import com.diegorezm.ratemymusic.modules.followers.data.repositories.FollowersRepository

suspend fun isFollowingUseCase(
    followDTO: FollowDTO,
    followersRepository: FollowersRepository
): Result<Boolean> {
    return try {
        val response = followersRepository.isFollower(followDTO)
        Result.success(response)
    } catch (e: Exception) {
        Log.e("isFollowingUseCase", e.message, e)
        Result.success(false)
    }
}