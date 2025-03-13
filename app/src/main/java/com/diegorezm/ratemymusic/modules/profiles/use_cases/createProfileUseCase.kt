package com.diegorezm.ratemymusic.modules.profiles.use_cases

import com.diegorezm.ratemymusic.modules.profiles.data_access.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.data_access.ProfileRepositoryImpl

suspend fun createProfileUseCase(
    uid: String,
    name: String,
    email: String,
    photoUrl: String?,
    repository: ProfileRepository = ProfileRepositoryImpl()
): Result<Unit> {
    return repository.create(uid, name, email, photoUrl)
}