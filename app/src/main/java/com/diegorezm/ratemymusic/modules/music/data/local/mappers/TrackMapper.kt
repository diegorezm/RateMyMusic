package com.diegorezm.ratemymusic.modules.music.data.local.mappers

import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackSimpleDTO
import com.diegorezm.ratemymusic.modules.music.domain.models.Track
import com.diegorezm.ratemymusic.modules.music.domain.models.TrackSimple

fun TrackSimpleDTO.toDomain(): TrackSimple {
    return TrackSimple(
        id = id,
        name = name,
        duration = durationMs,
    )
}

fun TrackDTO.toDomain(): Track {
    return Track(
        id = id,
        name = name,
        duration = durationMs,
        artists = artists.map { it.toDomain() },
        albumId = album.id,
        albumCoverURL = album.images.maxByOrNull { it.height }?.url ?: "",
        albumName = album.name
    )
}