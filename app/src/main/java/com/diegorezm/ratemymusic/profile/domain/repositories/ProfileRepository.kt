package com.diegorezm.ratemymusic.profile.domain.repositories

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.EmptyResult
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.profile.data.dto.ProfileDTO
import com.diegorezm.ratemymusic.profile.domain.models.Profile

interface ProfileRepository {
    suspend fun create(profile: ProfileDTO): EmptyResult<DataError>
    suspend fun checkIfProfileExists(uid: String): Result<Boolean, DataError>
    suspend fun getProfileById(uid: String): Result<Profile, DataError>
    suspend fun getProfileByIds(uids: List<String>): Result<List<Profile>, DataError>
}