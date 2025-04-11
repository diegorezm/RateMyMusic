package com.diegorezm.ratemymusic.user_favorites.data.repositories

import android.util.Log
import com.diegorezm.ratemymusic.core.data.RemoteErrorHandler
import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.EmptyResult
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.user_favorites.data.dto.FavoriteTypeDTO
import com.diegorezm.ratemymusic.user_favorites.data.dto.UserFavoriteDTO
import com.diegorezm.ratemymusic.user_favorites.data.mappers.toDomain
import com.diegorezm.ratemymusic.user_favorites.domain.UserFavorite
import com.diegorezm.ratemymusic.user_favorites.domain.UserFavorites
import com.diegorezm.ratemymusic.user_favorites.domain.UserFavoritesRepository
import io.github.jan.supabase.postgrest.Postgrest

class DefaultUserFavoritesRepository(
    private val db: Postgrest
) : UserFavoritesRepository {
    private val table = "user_favorites"

    override suspend fun create(
        userId: String,
        entityId: String,
        type: FavoriteTypeDTO
    ): EmptyResult<DataError> {
        return try {
            val favoriteDTO = UserFavoriteDTO(
                uid = userId,
                entityId = entityId,
                type = type
            )
            db.from(table).insert(favoriteDTO)
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        }
    }

    override suspend fun remove(favoriteId: Int): EmptyResult<DataError> {
        return try {
            db.from(table).delete {
                filter {
                    eq("id", favoriteId)

                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        }
    }

    override suspend fun remove(
        userId: String,
        entityId: String,
        type: FavoriteTypeDTO
    ): EmptyResult<DataError> {
        return try {
            db.from(table).delete {
                filter {
                    eq("uid", userId)
                    eq("entity_id", entityId)
                    eq("type", type.name.lowercase())
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        }
    }

    override suspend fun getById(favoriteId: Int): Result<UserFavorite, DataError> {
        return try {
            val result = db.from(table).select {
                filter {
                    eq("id", favoriteId)
                }
            }.decodeSingle<UserFavoriteDTO>()
            Result.Success(result.toDomain())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        }
    }

    override suspend fun getUserFavorites(userId: String): Result<UserFavorites, DataError> {
        return try {
            val result = db.from(table).select {
                filter {
                    eq("uid", userId)
                }
            }.decodeList<UserFavoriteDTO>()

            val albums = result.filter { it.type == FavoriteTypeDTO.ALBUM }
            val artists = result.filter { it.type == FavoriteTypeDTO.ARTIST }
            val tracks = result.filter { it.type == FavoriteTypeDTO.TRACK }

            val favorites = UserFavorites(
                albums = albums.map { it.toDomain() },
                artists = artists.map { it.toDomain() },
                tracks = tracks.map { it.toDomain() }
            )

            Result.Success(favorites)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        }
    }

    override suspend fun checkIfFavorite(
        userId: String,
        entityId: String,
        type: FavoriteTypeDTO
    ): Boolean {
        return try {
            val result = db.from(table).select {
                filter {
                    eq("uid", userId)
                    eq("entity_id", entityId)
                    eq("type", type.name.lowercase())
                }
            }.countOrNull()
            result != null && result.toInt() > 0
        } catch (e: Exception) {
            Log.e("UserFavoritesRepository", "checkIfFavorite: ${e.message}", e)
            false
        }
    }

}