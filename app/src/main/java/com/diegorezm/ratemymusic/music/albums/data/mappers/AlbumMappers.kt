package com.diegorezm.ratemymusic.music.albums.data.mappers

import com.diegorezm.ratemymusic.music.albums.data.dto.AlbumDTO
import com.diegorezm.ratemymusic.music.albums.domain.Album
import com.diegorezm.ratemymusic.music.artists.data.mappers.toDomain
import com.diegorezm.ratemymusic.music.tracks.data.mappers.toDomain

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
        tracks = tracks?.items?.map { it.toDomain() } ?: emptyList(),
        artists = artists.map { it.toDomain() },
        popularity = this.popularity ?: 0
    )
}