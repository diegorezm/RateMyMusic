package com.diegorezm.ratemymusic.modules.profiles.use_cases

import com.diegorezm.ratemymusic.modules.profiles.data_access.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.data_access.ProfileRepositoryImpl
import com.diegorezm.ratemymusic.modules.profiles.models.Profile

suspend fun getProfileUseCase(
    uid: String,
    repository: ProfileRepository = ProfileRepositoryImpl()
): Result<Profile?> {
    return repository.getByUserId(uid)
}