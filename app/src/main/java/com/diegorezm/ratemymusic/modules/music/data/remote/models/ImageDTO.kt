package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.google.gson.annotations.SerializedName

data class ImageDTO(
    @SerializedName("url") val url: String,
    @SerializedName("height") val height: Int,
    @SerializedName("width") val width: Int
)
