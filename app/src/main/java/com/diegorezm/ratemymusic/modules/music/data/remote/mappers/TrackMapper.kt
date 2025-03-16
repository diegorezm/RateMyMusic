package com.diegorezm.ratemymusic.modules.music.data.remote.mappers

import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackDTO
import com.diegorezm.ratemymusic.modules.music.domain.models.Track

fun TrackDTO.toDomain(): Track {
    return Track(
        id = id,
        name = name,
        duration = durationMs,
    )
}