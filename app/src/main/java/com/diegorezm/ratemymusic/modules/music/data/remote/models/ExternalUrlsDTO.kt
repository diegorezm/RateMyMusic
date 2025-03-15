package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.google.gson.annotations.SerializedName


data class ExternalUrlsDTO(
    @SerializedName("spotify") val spotify: String
)

