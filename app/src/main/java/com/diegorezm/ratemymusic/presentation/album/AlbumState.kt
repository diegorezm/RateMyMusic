package com.diegorezm.ratemymusic.presentation.album

import com.diegorezm.ratemymusic.modules.music.domain.models.Album

sealed class AlbumState {
    object Idle : AlbumState()
    object Loading : AlbumState()
    data class Success(val album: Album?) : AlbumState()
    data class Error(val message: String) : AlbumState()
}