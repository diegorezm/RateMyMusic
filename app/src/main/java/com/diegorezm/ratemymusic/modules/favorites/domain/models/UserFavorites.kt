package com.diegorezm.ratemymusic.modules.favorites.domain.models

data class UserFavorites(
    val tracks: List<UserFavorite> = emptyList(),
    val albums: List<UserFavorite> = emptyList(),
    val artists: List<UserFavorite> = emptyList()
)
