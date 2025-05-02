package com.diegorezm.ratemymusic.user_favorites.presentation

sealed interface UserFavoritesScreenActions {
    object OnRefreshRequested : UserFavoritesScreenActions
    data class OnAlbumClicked(val albumId: String) : UserFavoritesScreenActions
    data class OnTrackClicked(val trackId: String) : UserFavoritesScreenActions
    data class OnArtistClicked(val artistId: String) : UserFavoritesScreenActions
}