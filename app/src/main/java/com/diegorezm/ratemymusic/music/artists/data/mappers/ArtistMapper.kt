package com.diegorezm.ratemymusic.music.artists.data.mappers

import com.diegorezm.ratemymusic.modules.music.data.remote.models.ArtistDTO
import com.diegorezm.ratemymusic.music.artists.domain.Artist

fun ArtistDTO.toDomain(): Artist {
    val largestImage = this.images?.maxByOrNull { it.height }
    return Artist(
        id = id,
        name = name,
        externalUrl = externalUrls.spotify,
        imageURL = largestImage?.url,
        genres = genres ?: emptyList(),
        popularity = popularity ?: 0
    )
}