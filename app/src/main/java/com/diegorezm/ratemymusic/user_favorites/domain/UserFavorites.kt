package com.diegorezm.ratemymusic.user_favorites.domain

data class UserFavorites(
    val albums: List<UserFavorite>,
    val artists: List<UserFavorite>,
    val tracks: List<UserFavorite>,
)
