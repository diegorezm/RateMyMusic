package com.diegorezm.ratemymusic.modules.music.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class ExternalIdsDTO(
    val isrc: String?,
    val ean: String?,
    val upc: String?
) {
    constructor() : this(null, null, null)
}
