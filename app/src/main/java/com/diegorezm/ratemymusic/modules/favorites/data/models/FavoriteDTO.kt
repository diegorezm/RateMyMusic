package com.diegorezm.ratemymusic.modules.favorites.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteDTO(
    val uid: String,
    @SerialName("entity_id") val entityId: String,
    val type: FavoriteType,
)
