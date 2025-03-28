package com.diegorezm.ratemymusic.modules.music.domain.models

data class Artist(
    val id: String,
    val name: String,
    val externalUrl: String,
    val imageURL: String,
    val genres: List<String>,
    val popularity: Int,
)
