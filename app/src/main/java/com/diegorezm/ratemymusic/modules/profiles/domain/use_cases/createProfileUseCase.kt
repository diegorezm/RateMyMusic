package com.diegorezm.ratemymusic.modules.profiles.domain.use_cases

import android.util.Log
import com.diegorezm.ratemymusic.modules.common.PublicException
import com.diegorezm.ratemymusic.modules.profiles.data.models.ProfileDTO
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository

suspend fun createProfileUseCase(
    profileDTO: ProfileDTO,
    repository: ProfileRepository
): Result<Unit> {
    return try {
        val profileExists = checkIfProfileExistsUseCase(profileDTO.uid, repository)
        if (!profileExists) {
            repository.create(profileDTO)
        }
        return Result.success(Unit)
    } catch (e: PublicException) {
        Result.failure(e)
    } catch (e: Exception) {
        Log.e("createProfileUseCase", e.message, e)
        Result.failure(PublicException("Error creating profile."))
    }

}