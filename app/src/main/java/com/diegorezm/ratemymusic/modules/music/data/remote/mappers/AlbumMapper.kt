package com.diegorezm.ratemymusic.modules.music.data.remote.mappers

import com.diegorezm.ratemymusic.modules.music.data.remote.models.AlbumDTO
import com.diegorezm.ratemymusic.modules.music.domain.models.Album

fun AlbumDTO.toDomain(): Album {
    return Album(
        id = id,
        name = name,
        externalUrl = externalUrls.spotify,
        releaseDate = releaseDate,
        genres = genres,
        label = label,
        totalTracks = totalTracks
    )
}