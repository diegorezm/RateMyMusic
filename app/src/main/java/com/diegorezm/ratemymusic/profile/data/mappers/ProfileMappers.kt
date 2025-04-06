package com.diegorezm.ratemymusic.profile.data.mappers

import com.diegorezm.ratemymusic.profile.data.dto.ProfileDTO
import com.diegorezm.ratemymusic.profile.domain.models.Profile

fun ProfileDTO.toDomain(): Profile {
    return Profile(
        id = uid,
        name = name,
        email = email,
        avatarUrl = photoUrl,
        createdAt = createdAt
    )
}