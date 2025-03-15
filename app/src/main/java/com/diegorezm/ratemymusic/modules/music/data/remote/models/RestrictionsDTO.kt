package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.google.gson.annotations.SerializedName

data class RestrictionsDTO(
    @SerializedName("reason") val reason: String
)

