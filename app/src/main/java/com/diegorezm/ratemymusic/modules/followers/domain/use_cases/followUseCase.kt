package com.diegorezm.ratemymusic.modules.followers.domain.use_cases

import com.diegorezm.ratemymusic.modules.followers.data.models.FollowDTO
import com.diegorezm.ratemymusic.modules.followers.data.repositories.FollowersRepository
import com.diegorezm.ratemymusic.modules.followers.domain.repositories.FollowersRepositoryImpl

suspend fun followUseCase(
    followDTO: FollowDTO,
    followersRepository: FollowersRepository = FollowersRepositoryImpl()
): Result<Unit> {
    return followersRepository.follow(followDTO)
}