package com.diegorezm.ratemymusic.presentation.user_favorites

import com.diegorezm.ratemymusic.modules.favorites.domain.models.UserFavorites

sealed class UserFavoritesState {
    object Idle : UserFavoritesState()
    object Loading : UserFavoritesState()
    data class Success(val favorites: UserFavorites) : UserFavoritesState()
    data class Error(val message: String) : UserFavoritesState()
}