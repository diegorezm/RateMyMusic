package com.diegorezm.ratemymusic.music.artists.data.dto

import com.diegorezm.ratemymusic.modules.music.data.remote.models.ArtistDTO
import kotlinx.serialization.Serializable

@Serializable
data class ArtistBulkDTO(
    val artists: List<ArtistDTO>
)
