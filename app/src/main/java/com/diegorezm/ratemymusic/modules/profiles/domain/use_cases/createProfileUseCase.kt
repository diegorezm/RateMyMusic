package com.diegorezm.ratemymusic.modules.profiles.domain.use_cases

import android.util.Log
import com.diegorezm.ratemymusic.modules.profiles.data.models.ProfileDTO
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.utils.handleResult

suspend fun createProfileUseCase(
    profileDTO: ProfileDTO,
    repository: ProfileRepository
): Result<Unit> {
    return handleResult(tag = "createProfileUseCase") {
        val profileExists = checkIfProfileExistsUseCase(profileDTO.uid, repository).getOrThrow()
        if (!profileExists) {
            Log.d("createProfileUseCase", "Creating profile: ${profileDTO.uid}")
            repository.create(profileDTO)
        } else {
            Log.d("createProfileUseCase", "Profile already exists: ${profileDTO.uid}")
        }
        Result.success(Unit)
    }

}