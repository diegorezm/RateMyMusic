package com.diegorezm.ratemymusic.music.artists.presentation

sealed interface ArtistScreenActions {
    object OnBackClick : ArtistScreenActions
    object OnAddToFavoritesClick : ArtistScreenActions
    object OnRemoveFromFavoritesClick : ArtistScreenActions
    object OnOpenReviewsDrawer : ArtistScreenActions
    object OnCloseReviewsDrawer : ArtistScreenActions
}