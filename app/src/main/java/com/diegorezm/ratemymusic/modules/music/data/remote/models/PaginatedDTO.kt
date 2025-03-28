package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.google.gson.annotations.SerializedName

data class PaginatedDTO<T>(
    @SerializedName("href") val href: String,
    @SerializedName("limit") val limit: Int = 10,
    @SerializedName("next") val next: String?,
    @SerializedName("offset") val offset: Int = 0,
    @SerializedName("previous") val previous: String?,
    @SerializedName("total") val total: Int = 0,
    @SerializedName("items") val items: List<T>
) {
}