package com.diegorezm.ratemymusic.modules.profiles.data.repositories

import com.diegorezm.ratemymusic.modules.profiles.data.models.ProfileDTO
import com.diegorezm.ratemymusic.modules.profiles.domain.models.Profile

interface ProfileRepository {
    suspend fun create(
        profile: ProfileDTO
    )

    suspend fun checkIfProfileExists(uid: String): Boolean
    suspend fun getProfileById(uid: String): Profile
    suspend fun getProfileByIds(uids: List<String>): List<Profile>
}
