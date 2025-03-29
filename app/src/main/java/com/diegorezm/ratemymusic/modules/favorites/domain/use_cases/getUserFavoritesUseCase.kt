package com.diegorezm.ratemymusic.modules.favorites.domain.use_cases

import com.diegorezm.ratemymusic.modules.favorites.data.repositories.FavoritesRepository
import com.diegorezm.ratemymusic.modules.favorites.domain.models.UserFavorites
import com.diegorezm.ratemymusic.utils.handleResult

suspend fun getUserFavoritesUseCase(
    userId: String,
    favoritesRepository: FavoritesRepository
): Result<UserFavorites> {
    return handleResult(tag = "getUserFavoritesUseCase") {
        favoritesRepository.getUserFavorites(userId)
    }
}