package com.diegorezm.ratemymusic.user_favorites.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserFavoriteDTO(
    val id: Int? = null,
    val uid: String,
    @SerialName("entity_id") val entityId: String,
    @SerialName("type") val type: FavoriteTypeDTO,
)
