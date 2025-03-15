package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.google.gson.annotations.SerializedName

data class CopyrightDTO(
    @SerializedName("text") val text: String,
    @SerializedName("type") val type: String
)
