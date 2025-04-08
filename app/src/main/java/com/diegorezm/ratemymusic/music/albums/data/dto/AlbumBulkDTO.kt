package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.diegorezm.ratemymusic.music.albums.data.dto.AlbumDTO
import kotlinx.serialization.Serializable

@Serializable
data class AlbumBulkDTO(
    val albums: List<AlbumDTO>
)
