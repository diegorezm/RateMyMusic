package com.diegorezm.ratemymusic.modules.profiles.domain.use_cases

import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.domain.repositories.ProfileRepositoryImpl

suspend fun checkIfProfileExistsUseCase(
    uid: String,
    repository: ProfileRepository = ProfileRepositoryImpl()
): Boolean {
    return repository.checkIfProfileExists(uid)
}