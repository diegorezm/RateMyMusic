package com.diegorezm.ratemymusic.modules.profiles.domain.use_cases

import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.domain.models.Profile
import com.diegorezm.ratemymusic.utils.handleResult

suspend fun getProfileUseCase(
    uid: String,
    repository: ProfileRepository
): Result<Profile> {
    return handleResult(tag = "getProfileUseCase") {
        repository.getProfileById(uid)
    }
}