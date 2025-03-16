package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.google.gson.annotations.SerializedName

data class ArtistDTO(
    @SerializedName("id") val id: String,
    @SerializedName("external_urls") val externalUrls: ExternalUrlsDTO,
    @SerializedName("href") val href: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("uri") val uri: String,
    @SerializedName("genres") val genres: List<String>,
    @SerializedName("images") val images: List<ImageDTO>,
    @SerializedName("popularity") val popularity: Int
)
