package com.diegorezm.ratemymusic.modules.favorites.domain.models

import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserFavorite(
    val id: Int,
    val type: FavoriteType,
    @SerialName("entity_id") val entityId: String,
    val uid: String
)