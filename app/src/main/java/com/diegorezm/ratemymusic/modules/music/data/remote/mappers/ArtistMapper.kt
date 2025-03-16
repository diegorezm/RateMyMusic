package com.diegorezm.ratemymusic.modules.music.data.remote.mappers

import com.diegorezm.ratemymusic.modules.music.data.remote.models.ArtistDTO
import com.diegorezm.ratemymusic.modules.music.domain.models.Artist

fun ArtistDTO.toDomain(): Artist {
    return Artist(
        name = this.name,
        id = this.id,
        externalUrl = this.externalUrls.spotify,
        imageURL = this.images.firstOrNull()?.url ?: "",
        genres = this.genres
    )
}