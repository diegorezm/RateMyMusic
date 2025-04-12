package com.diegorezm.ratemymusic.user_favorites.domain

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.EmptyResult
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.user_favorites.data.dto.FavoriteTypeDTO
import kotlinx.coroutines.flow.Flow

interface UserFavoritesRepository {
    suspend fun create(
        userId: String,
        entityId: String,
        type: FavoriteTypeDTO
    ): EmptyResult<DataError>

    suspend fun remove(favoriteId: Int): EmptyResult<DataError>

    suspend fun remove(
        userId: String,
        entityId: String,
        type: FavoriteTypeDTO
    ): EmptyResult<DataError>

    suspend fun getById(favoriteId: Int): Result<UserFavorite, DataError>
    suspend fun getUserFavorites(userId: String): Result<UserFavorites, DataError>
     fun checkIfFavorite(
        userId: String,
        entityId: String,
        type: FavoriteTypeDTO
    ): Flow<Boolean>
}