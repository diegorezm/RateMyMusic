package com.diegorezm.ratemymusic.modules.music.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDTO(
    @SerialName("id") val id: String,
    @SerialName("external_urls") val externalUrls: ExternalUrlsDTO,
    @SerialName("href") val href: String,
    @SerialName("name") val name: String,
    @SerialName("type") val type: String,
    @SerialName("uri") val uri: String,

    @SerialName("images") val images: List<ImageDTO>? = null,
    @SerialName("genres") val genres: List<String>? = null,
    @SerialName("popularity") val popularity: Int? = null,
)

