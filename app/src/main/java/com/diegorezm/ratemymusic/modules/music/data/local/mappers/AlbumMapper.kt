package com.diegorezm.ratemymusic.modules.music.data.local.mappers

import com.diegorezm.ratemymusic.modules.music.data.remote.models.AlbumDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.AlbumSimpleDTO
import com.diegorezm.ratemymusic.modules.music.domain.models.Album
import com.diegorezm.ratemymusic.modules.music.domain.models.AlbumSimple

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
        tracks = this.tracks.toDomain {
            it.toDomain()
        },
        artists = artists.map { it.toDomain() },
        popularity = popularity

    )
}

fun AlbumSimpleDTO.toDomain(): AlbumSimple {
    val largestHeightImage = this.images.maxByOrNull { it.height }
    return AlbumSimple(
        id = id,
        imageURL = largestHeightImage?.url,
        name = name,
        releaseDate = releaseDate,
        totalTracks = totalTracks,
        artists = artists.map { it.toDomain() },
        externalUrl = externalUrls.spotify
    )
}