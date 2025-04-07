package com.diegorezm.ratemymusic.modules.music.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class RestrictionsDTO(
    val reason: String?
)

