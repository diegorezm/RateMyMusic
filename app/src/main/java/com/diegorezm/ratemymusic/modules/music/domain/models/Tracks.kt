package com.diegorezm.ratemymusic.modules.music.domain.models

data class Tracks(
    val href: String,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val items: List<Track>
)