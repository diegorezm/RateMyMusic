package com.diegorezm.ratemymusic.modules.music.data.local.mappers

import com.diegorezm.ratemymusic.modules.music.data.remote.models.ArtistDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.ArtistSimpleDTO
import com.diegorezm.ratemymusic.modules.music.domain.models.Artist
import com.diegorezm.ratemymusic.modules.music.domain.models.ArtistSimple

fun ArtistDTO.toDomain(): Artist {
    return Artist(
        name = this.name,
        id = this.id,
        externalUrl = this.externalUrls.spotify,
        genres = genres,
        popularity = popularity,
        imageURL = images.maxByOrNull { it.height }?.url ?: ""
    )
}

fun ArtistSimpleDTO.toDomain(): ArtistSimple {
    return ArtistSimple(
        name = this.name,
        id = this.id,
        externalUrl = this.externalUrls.spotify,
    )
}

