package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.google.gson.annotations.SerializedName

data class ArtistSimpleDTO(
    @SerializedName("id") val id: String,
    @SerializedName("external_urls") val externalUrls: ExternalUrlsDTO,
    @SerializedName("href") val href: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("uri") val uri: String,
)
