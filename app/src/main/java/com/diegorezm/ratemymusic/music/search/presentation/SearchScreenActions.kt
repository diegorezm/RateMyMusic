package com.diegorezm.ratemymusic.music.search.presentation

sealed interface SearchScreenActions {
    data class OnAlbumClick(val albumId: String) : SearchScreenActions
    data class OnTrackClick(val trackId: String) : SearchScreenActions
    data class OnArtistClick(val artistId: String) : SearchScreenActions

    data class OnQueryChange(val query: String) : SearchScreenActions
    data class OnFilterChange(val type: SearchType) : SearchScreenActions
}