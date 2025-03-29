package com.diegorezm.ratemymusic.modules.followers.domain.use_cases

import android.util.Log
import com.diegorezm.ratemymusic.modules.common.PublicException
import com.diegorezm.ratemymusic.modules.followers.data.models.FollowDTO
import com.diegorezm.ratemymusic.modules.followers.data.repositories.FollowersRepository

suspend fun followUseCase(
    followDTO: FollowDTO,
    followersRepository: FollowersRepository
): Result<Unit> {
    return try {
        followersRepository.follow(followDTO)
        Result.success(Unit)
    } catch (e: PublicException) {
        Result.failure(e)
    } catch (e: Exception) {
        Log.e("followUseCase", e.message, e)
        Result.failure(PublicException("Error getting following count."))
    }
}