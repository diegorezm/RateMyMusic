package com.diegorezm.ratemymusic.music.albums.data.dto

import com.diegorezm.ratemymusic.modules.music.data.remote.models.PaginatedDTO
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedAlbumDTO(
    val albums: PaginatedDTO<AlbumDTO>
)
