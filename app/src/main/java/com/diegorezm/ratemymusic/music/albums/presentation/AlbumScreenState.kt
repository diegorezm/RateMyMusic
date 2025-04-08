package com.diegorezm.ratemymusic.music.albums.presentation

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.music.albums.domain.Album

sealed class AlbumScreenState {
    object Idle : AlbumScreenState()
    object Loading : AlbumScreenState()
    data class Success(val album: Album) : AlbumScreenState()
    data class Error(val error: DataError) : AlbumScreenState()
}
