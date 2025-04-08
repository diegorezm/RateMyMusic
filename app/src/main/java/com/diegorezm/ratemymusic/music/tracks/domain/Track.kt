package com.diegorezm.ratemymusic.music.tracks.domain

import com.diegorezm.ratemymusic.music.artists.domain.Artist

data class Track(
    val id: String,
    val name: String,
    val externalUrl: String,
    val duration: Int,
    val artists: List<Artist>,
    val albumId: String? = null,
    val albumCoverURL: String? = null,
    val albumName: String? = null
)

