package com.diegorezm.ratemymusic.music.albums.presentation

sealed interface AlbumScreenActions {
    data object OnBackClick : AlbumScreenActions
    data class OnTrackClick(val trackId: String) : AlbumScreenActions

    data object OnAddToFavoritesClick : AlbumScreenActions
    data object OnRemoveFromFavoritesClick : AlbumScreenActions

    data object OnOpenReviewsDrawer : AlbumScreenActions
    data object OnCloseReviewsDrawer : AlbumScreenActions
}