package com.diegorezm.ratemymusic.modules.music.domain.models

data class SearchRequest(
    val query: String,
    val limit: Int,
    val offset: Int,
    val spotifyAuthToken: String? = null
)
