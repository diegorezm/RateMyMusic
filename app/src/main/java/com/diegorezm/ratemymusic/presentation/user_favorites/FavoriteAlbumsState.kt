package com.diegorezm.ratemymusic.presentation.user_favorites

import com.diegorezm.ratemymusic.modules.music.domain.models.Album

sealed class FavoriteAlbumsState {
    object Idle : FavoriteAlbumsState()
    object Loading : FavoriteAlbumsState()
    data class Success(val albums: List<Album>) : FavoriteAlbumsState()
    data class Error(val message: String) : FavoriteAlbumsState()
}
