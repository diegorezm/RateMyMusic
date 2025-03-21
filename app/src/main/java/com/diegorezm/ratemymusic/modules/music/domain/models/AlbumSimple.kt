package com.diegorezm.ratemymusic.modules.music.domain.models

data class AlbumSimple(
    val id: String,
    val name: String,
    val externalUrl: String,
    val releaseDate: String,
    val totalTracks: Int,
    val imageURL: String?,
    val artists: List<ArtistSimple> = emptyList()
)
