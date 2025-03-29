package com.diegorezm.ratemymusic.modules.favorites.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class FavoriteType {
    @SerialName("track")
    TRACK,

    @SerialName("album")
    ALBUM,

    @SerialName("artist")
    ARTIST;
}