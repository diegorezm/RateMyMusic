package com.diegorezm.ratemymusic.music.artists.data.dto

import com.diegorezm.ratemymusic.modules.music.data.remote.models.ArtistDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.PaginatedDTO
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedArtistDTO(
    val artists: PaginatedDTO<ArtistDTO>
)
