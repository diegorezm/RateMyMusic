package com.diegorezm.ratemymusic.music.tracks.data.dto

import com.diegorezm.ratemymusic.modules.music.data.remote.models.PaginatedDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackDTO
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedTrackDTO(
    val tracks: PaginatedDTO<TrackDTO>
)
