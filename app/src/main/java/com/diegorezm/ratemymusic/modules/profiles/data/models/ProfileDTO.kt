package com.diegorezm.ratemymusic.modules.profiles.data.models

import com.diegorezm.ratemymusic.modules.profiles.domain.models.Profile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDTO(
    val uid: String,
    val name: String,
    val email: String,
    @SerialName("photo_url") val photoUrl: String?
)

fun ProfileDTO.toDomain(): Profile {
    return Profile(
        uid = this.uid,
        name = this.name,
        email = this.email,
        photoUrl = this.photoUrl
    )
}