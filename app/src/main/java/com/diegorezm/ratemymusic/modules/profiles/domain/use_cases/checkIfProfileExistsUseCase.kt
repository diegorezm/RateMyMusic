package com.diegorezm.ratemymusic.modules.profiles.domain.use_cases

import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository

suspend fun checkIfProfileExistsUseCase(
    uid: String,
    repository: ProfileRepository
): Boolean {
    return repository.checkIfProfileExists(uid)
}