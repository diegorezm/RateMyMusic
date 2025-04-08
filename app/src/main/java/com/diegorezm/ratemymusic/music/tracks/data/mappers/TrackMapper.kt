package com.diegorezm.ratemymusic.music.tracks.data.mappers

import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackDTO
import com.diegorezm.ratemymusic.music.artists.data.mappers.toDomain
import com.diegorezm.ratemymusic.music.tracks.domain.Track

fun TrackDTO.toDomain(): Track {
    return Track(
        id = id,
        name = name,
        externalUrl = externalUrls.spotify,
        duration = durationMs,
        artists = artists.map { it.toDomain() },
        albumId = album?.id,
        albumCoverURL = album?.images?.maxByOrNull { it.height }?.url,
        albumName = album?.name
    )
}