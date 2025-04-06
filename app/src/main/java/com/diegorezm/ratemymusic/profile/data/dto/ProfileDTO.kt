package com.diegorezm.ratemymusic.profile.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ProfileDTO(
    @SerialName("uid") val uid: String,
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("photo_url") val photoUrl: String?,
    @SerialName("created_at") val createdAt: String = Date().toString()
)