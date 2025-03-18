package com.diegorezm.ratemymusic.modules.favorites.domain.use_cases

import com.diegorezm.ratemymusic.modules.favorites.data.repositories.FavoritesRepository
import com.diegorezm.ratemymusic.modules.favorites.domain.models.UserFavorites
import com.diegorezm.ratemymusic.modules.favorites.domain.repositories.FavoritesRepositoryImpl

suspend fun getUserFavoritesUseCase(
    userId: String,
    favoritesRepository: FavoritesRepository = FavoritesRepositoryImpl()
): Result<UserFavorites> {
    return favoritesRepository.getUserFavorites(userId)
}