package com.diegorezm.ratemymusic.music.albums.domain

data class Album(
    val id: String,
    val name: String,
    val externalUrl: String,
    val releaseDate: String,
    val genres: List<String>,
    val label: String,
    val totalTracks: Int,
    val imageURL: String?,
    val tracks: List<String> = emptyList(),
    val artists: List<String> = emptyList(),
    val popularity: Int,
)