package com.diegorezm.ratemymusic.modules.music.data.remote.mappers

import com.diegorezm.ratemymusic.modules.music.data.remote.models.AlbumDTO
import com.diegorezm.ratemymusic.modules.music.domain.models.Album

fun AlbumDTO.toDomain(): Album {
    val largestHeightImage = this.images.maxByOrNull { it.height }

    return Album(
        id = id,
        name = name,
        externalUrl = externalUrls.spotify,
        releaseDate = releaseDate,
        genres = genres,
        label = label,
        totalTracks = totalTracks,
        imageURL = largestHeightImage?.url,
        tracks = this.tracks.toDomain()
    )
}