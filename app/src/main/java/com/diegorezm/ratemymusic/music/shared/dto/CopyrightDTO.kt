package com.diegorezm.ratemymusic.modules.music.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class CopyrightDTO(
    val text: String,
    val type: String
)
