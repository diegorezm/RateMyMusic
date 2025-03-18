package com.diegorezm.ratemymusic.modules.favorites.domain.use_cases

import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteDTO
import com.diegorezm.ratemymusic.modules.favorites.data.repositories.FavoritesRepository
import com.diegorezm.ratemymusic.modules.favorites.domain.repositories.FavoritesRepositoryImpl

suspend fun checkIfFavoriteUseCase(
    favoriteDTO: FavoriteDTO,
    favoritesRepository: FavoritesRepository = FavoritesRepositoryImpl()
): Result<Boolean> {
    return favoritesRepository.checkIfFavorite(favoriteDTO)
}