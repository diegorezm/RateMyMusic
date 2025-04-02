package com.diegorezm.ratemymusic.presentation.home

import com.diegorezm.ratemymusic.modules.music.domain.models.AlbumSimple

sealed class HomeState {
    object Idle : HomeState()
    object Loading : HomeState()
    data class Success(val albums: List<AlbumSimple>) : HomeState()
    data class Error(val message: String) : HomeState()
}