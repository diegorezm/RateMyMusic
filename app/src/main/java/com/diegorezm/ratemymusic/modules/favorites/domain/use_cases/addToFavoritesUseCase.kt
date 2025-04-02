package com.diegorezm.ratemymusic.modules.favorites.domain.use_cases

import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteDTO
import com.diegorezm.ratemymusic.modules.favorites.data.repositories.FavoritesRepository
import com.diegorezm.ratemymusic.utils.handleResult

suspend fun addToFavoritesUseCase(
    favoriteDTO: FavoriteDTO,
    favoritesRepository: FavoritesRepository
): Result<Unit> {
    return handleResult(tag = "addToFavoritesUseCase") {
        favoritesRepository.create(favoriteDTO)
    }
}


