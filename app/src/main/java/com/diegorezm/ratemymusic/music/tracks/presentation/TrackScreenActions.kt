package com.diegorezm.ratemymusic.music.tracks.presentation

sealed interface TrackScreenActions {
    data object OnBackClick : TrackScreenActions

    data object OnAddToFavoritesClick : TrackScreenActions
    data object OnRemoveFromFavoritesClick : TrackScreenActions

    data object OnOpenReviewsDrawer : TrackScreenActions
    data object OnCloseReviewsDrawer : TrackScreenActions
}