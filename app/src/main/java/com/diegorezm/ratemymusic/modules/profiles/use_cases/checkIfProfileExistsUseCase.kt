package com.diegorezm.ratemymusic.modules.profiles.use_cases

import com.diegorezm.ratemymusic.modules.profiles.data_access.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.data_access.ProfileRepositoryImpl

suspend fun checkIfProfileExistsUseCase(
    uid: String,
    repository: ProfileRepository = ProfileRepositoryImpl()
): Boolean {
    return repository.checkIfProfileExists(uid)
}