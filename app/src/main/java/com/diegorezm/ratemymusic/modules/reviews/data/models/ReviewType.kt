package com.diegorezm.ratemymusic.modules.reviews.data.models

import kotlinx.serialization.Serializable

@Serializable
enum class ReviewType {
    TRACK,
    ALBUM,
    ARTIST
}
