package com.diegorezm.ratemymusic.modules.profiles.data.repositories

import com.diegorezm.ratemymusic.modules.profiles.data.models.ProfileDTO
import com.diegorezm.ratemymusic.modules.profiles.domain.models.Profile

interface ProfileRepository {
    suspend fun create(
        profile: ProfileDTO
    ): Result<Unit>

    suspend fun checkIfProfileExists(uid: String): Boolean
    suspend fun getByUserId(uid: String): Result<Profile?>
}
