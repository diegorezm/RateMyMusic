package com.diegorezm.ratemymusic.modules.profiles.domain.use_cases

import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.utils.handleResult

suspend fun checkIfProfileExistsUseCase(
    uid: String,
    repository: ProfileRepository
): Result<Boolean> {
    return handleResult {
        repository.checkIfProfileExists(uid)
    }
}