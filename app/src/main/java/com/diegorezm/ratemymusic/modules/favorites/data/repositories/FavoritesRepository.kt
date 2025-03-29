package com.diegorezm.ratemymusic.modules.favorites.data.repositories

import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteDTO
import com.diegorezm.ratemymusic.modules.favorites.domain.models.UserFavorite
import com.diegorezm.ratemymusic.modules.favorites.domain.models.UserFavorites

interface FavoritesRepository {
    suspend fun create(favoriteDTO: FavoriteDTO)
    suspend fun remove(favoriteId: Int)
    suspend fun getById(favoriteId: Int): UserFavorite
    suspend fun getUserFavorites(userId: String): UserFavorites
    suspend fun checkIfFavorite(userId: String, entityId: String): UserFavorite?
}