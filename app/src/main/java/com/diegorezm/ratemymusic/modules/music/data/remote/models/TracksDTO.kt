package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.google.gson.annotations.SerializedName

data class TracksDTO(
    @SerializedName("href") val href: String,
    @SerializedName("limit") val limit: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("offset") val offset: Int,
    @SerializedName("previous") val previous: String?,
    @SerializedName("total") val total: Int,
    @SerializedName("items") val items: List<TrackSimpleDTO>
)

