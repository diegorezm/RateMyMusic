package com.diegorezm.ratemymusic.modules.music.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class ImageDTO(
    val url: String,
    val height: Int,
    val width: Int
)
