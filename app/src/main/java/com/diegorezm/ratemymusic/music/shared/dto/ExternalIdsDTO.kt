package com.diegorezm.ratemymusic.modules.music.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class ExternalIdsDTO(
    val isrc: String? = null,
    val ean: String? = null,
    val upc: String? = null
)
