package com.diegorezm.ratemymusic.modules.music.domain.models

data class Track(
    val id: String,
    val name: String,
    val duration: Int,
    val artists: List<ArtistSimple>,
    val albumId: String,
    val albumCoverURL: String,
    val albumName: String
)
