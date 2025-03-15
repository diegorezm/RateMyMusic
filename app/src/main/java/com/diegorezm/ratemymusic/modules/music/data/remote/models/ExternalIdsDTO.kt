package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.google.gson.annotations.SerializedName

data class ExternalIdsDTO(
    @SerializedName("isrc") val isrc: String?,
    @SerializedName("ean") val ean: String?,
    @SerializedName("upc") val upc: String?
)
