package com.diegorezm.ratemymusic.modules.music.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LinkedFromDTO(
    @SerialName("external_urls") val externalUrls: ExternalUrlsDTO,
    val href: String,
    val id: String,
    val type: String,
    val uri: String
)
