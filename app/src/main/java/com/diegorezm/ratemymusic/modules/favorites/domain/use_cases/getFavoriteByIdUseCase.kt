package com.diegorezm.ratemymusic.modules.favorites.domain.use_cases

import com.diegorezm.ratemymusic.modules.favorites.data.repositories.FavoritesRepository
import com.diegorezm.ratemymusic.modules.favorites.domain.models.UserFavorite
import com.diegorezm.ratemymusic.utils.handleResult

suspend fun getFavoriteByIdUseCase(
    favoriteId: Int,
    favoritesRepository: FavoritesRepository
): Result<UserFavorite> {
    return handleResult(tag = "getFavoriteByIdUseCase") {
        favoritesRepository.getById(favoriteId)
    }
}