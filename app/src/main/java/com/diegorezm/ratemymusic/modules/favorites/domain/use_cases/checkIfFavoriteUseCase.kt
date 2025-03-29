package com.diegorezm.ratemymusic.modules.favorites.domain.use_cases

import com.diegorezm.ratemymusic.modules.favorites.data.repositories.FavoritesRepository
import com.diegorezm.ratemymusic.modules.favorites.domain.models.UserFavorite
import com.diegorezm.ratemymusic.utils.handleResult

suspend fun checkIfFavoriteUseCase(
    userId: String,
    entityId: String,
    favoritesRepository: FavoritesRepository
): Result<UserFavorite?> {
    return handleResult(tag = "checkIfFavoriteUseCase") {
        favoritesRepository.checkIfFavorite(userId, entityId)
    }
}