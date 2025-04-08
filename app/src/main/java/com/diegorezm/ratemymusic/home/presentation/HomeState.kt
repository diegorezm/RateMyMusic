package com.diegorezm.ratemymusic.home.presentation

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.music.albums.domain.Album

sealed class HomeState {
    object Idle : HomeState()
    object Loading : HomeState()
    data class Success(val albums: List<Album>) : HomeState()
    data class Error(val error: DataError) : HomeState()
}