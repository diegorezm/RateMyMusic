package com.diegorezm.ratemymusic.modules.profiles.domain.use_cases

import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.domain.models.Profile
import com.diegorezm.ratemymusic.modules.profiles.domain.repositories.ProfileRepositoryImpl

suspend fun getProfileUseCase(
    uid: String,
    repository: ProfileRepository = ProfileRepositoryImpl()
): Result<Profile?> {
    return repository.getProfileById(uid)
}