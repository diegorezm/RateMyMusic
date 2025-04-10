package com.diegorezm.ratemymusic.reviews.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ReviewEntityDTO {
    @SerialName("track")
    TRACK,

    @SerialName("album")
    ALBUM,

    @SerialName("artist")
    ARTIST
}