package com.diegorezm.ratemymusic.modules.profiles.data.models

import com.diegorezm.ratemymusic.modules.profiles.domain.models.Profile

data class ProfileDTO(
    val uid: String,
    val name: String,
    val email: String,
    val photoUrl: String?
)

fun ProfileDTO.toDomain(): Profile {
    return Profile(
        uid = this.uid,
        name = this.name,
        email = this.email,
        photoUrl = this.photoUrl
    )
}