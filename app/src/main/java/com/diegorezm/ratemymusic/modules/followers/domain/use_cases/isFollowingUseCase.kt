package com.diegorezm.ratemymusic.modules.followers.domain.use_cases

import com.diegorezm.ratemymusic.modules.followers.data.repositories.FollowersRepository
import com.diegorezm.ratemymusic.modules.followers.domain.repositories.FollowersRepositoryImpl

suspend fun isFollowingUseCase(
    followingId: String,
    followerId: String,
    followersRepository: FollowersRepository = FollowersRepositoryImpl()
): Result<Boolean> {
    return followersRepository.isFollower(followingId, followerId)
}