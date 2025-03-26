package com.diegorezm.ratemymusic.presentation.search

import com.diegorezm.ratemymusic.modules.music.domain.models.Album

sealed class AlbumSearchState {
    object Idle : AlbumSearchState()
    object Loading : AlbumSearchState()
    data class Success(val albums: List<Album>) : AlbumSearchState()
    data class Error(val message: String) : AlbumSearchState()
}