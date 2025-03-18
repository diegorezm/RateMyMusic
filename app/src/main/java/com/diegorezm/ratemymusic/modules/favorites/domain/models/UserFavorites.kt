package com.diegorezm.ratemymusic.modules.favorites.domain.models

data class UserFavorites(
    val tracks: List<String> = emptyList(),
    val albums: List<String> = emptyList(),
    val artists: List<String> = emptyList()
)
