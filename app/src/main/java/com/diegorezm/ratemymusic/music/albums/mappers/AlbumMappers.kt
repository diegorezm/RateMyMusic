package com.diegorezm.ratemymusic.music.albums.mappers

import com.diegorezm.ratemymusic.music.albums.data.dto.AlbumDTO
import com.diegorezm.ratemymusic.music.albums.domain.Album

fun AlbumDTO.toDomain(): Album {
    val largestImage = this.images.maxByOrNull { it.height }
    return Album(
        id = this.id,
        name = this.name,
        externalUrl = this.externalUrls.spotify,
        releaseDate = this.releaseDate,
        genres = this.genres ?: emptyList(),
        label = this.label ?: "",
        totalTracks = this.totalTracks,
        imageURL = largestImage?.url,
        // TODO: Get parse tracks
        tracks = emptyList(),
        artists = emptyList(),
        popularity = this.popularity ?: 0
    )
}