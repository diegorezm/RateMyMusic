package com.diegorezm.ratemymusic.user_favorites.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class FavoriteTypeDTO() {
    @SerialName("track")
    TRACK,

    @SerialName("album")
    ALBUM,

    @SerialName("artist")
    ARTIST
}