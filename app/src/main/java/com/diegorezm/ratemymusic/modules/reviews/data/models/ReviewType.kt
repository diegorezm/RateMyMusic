package com.diegorezm.ratemymusic.modules.reviews.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ReviewType {
    @SerialName("track")
    TRACK,
    @SerialName("album")
    ALBUM,
    @SerialName("artist")
    ARTIST
}
