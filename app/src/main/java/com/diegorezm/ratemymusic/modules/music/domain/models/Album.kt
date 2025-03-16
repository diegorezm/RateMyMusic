package com.diegorezm.ratemymusic.modules.music.domain.models

data class Album(
    val id: String,
    val name: String,
    val externalUrl: String,
    val releaseDate: String,
    val genres: List<String>,
    val label: String,
    val totalTracks: Int,
    val imageURL: String?,
    val tracks: Tracks
)
