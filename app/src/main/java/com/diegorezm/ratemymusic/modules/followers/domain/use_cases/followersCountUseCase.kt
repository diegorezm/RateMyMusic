package com.diegorezm.ratemymusic.modules.followers.domain.use_cases

import com.diegorezm.ratemymusic.modules.followers.data.repositories.FollowersRepository
import com.diegorezm.ratemymusic.modules.followers.domain.repositories.FollowersRepositoryImpl

suspend fun getFollowersCountUseCase(
    userId: String,
    followersRepository: FollowersRepository = FollowersRepositoryImpl()
): Result<Int> {
    return followersRepository.getFollowersCount(userId)
}