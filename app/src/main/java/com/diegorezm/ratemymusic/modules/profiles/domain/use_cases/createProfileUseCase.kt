package com.diegorezm.ratemymusic.modules.profiles.domain.use_cases

import com.diegorezm.ratemymusic.modules.profiles.data.models.ProfileDTO
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.domain.repositories.ProfileRepositoryImpl

suspend fun createProfileUseCase(
    uid: String,
    name: String,
    email: String,
    photoUrl: String?,
    repository: ProfileRepository = ProfileRepositoryImpl()
): Result<Unit> {
    val dto = ProfileDTO(uid, name, email, photoUrl)
    return repository.create(dto)
}