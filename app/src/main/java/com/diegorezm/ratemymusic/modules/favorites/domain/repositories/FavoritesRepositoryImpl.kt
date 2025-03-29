package com.diegorezm.ratemymusic.modules.favorites.domain.repositories

import com.diegorezm.ratemymusic.modules.common.PublicException
import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteDTO
import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteType
import com.diegorezm.ratemymusic.modules.favorites.data.repositories.FavoritesRepository
import com.diegorezm.ratemymusic.modules.favorites.domain.models.UserFavorite
import com.diegorezm.ratemymusic.modules.favorites.domain.models.UserFavorites
import io.github.jan.supabase.postgrest.Postgrest
import java.util.UUID

class FavoritesRepositoryImpl(
    private val db: Postgrest
) : FavoritesRepository {
    private val table = "user_favorites"

    override suspend fun create(favoriteDTO: FavoriteDTO) {
        db.from(table).insert(favoriteDTO)
    }

    override suspend fun remove(favoriteId: Int) {
        db.from(table).delete {
            filter {
                eq("id", favoriteId)
            }
        }
    }

    override suspend fun getById(favoriteId: Int): UserFavorite {
        val response = db.from(table).select {
            filter {
                eq("id", favoriteId)
            }
        }.decodeAsOrNull<UserFavorite>()
        if (response == null) throw PublicException("Favorite not found")
        return response
    }


    override suspend fun getUserFavorites(userId: String): UserFavorites {
        val allFavorites = db.from(table).select {
            filter {
                eq("uid", userId)
            }
        }.decodeList<UserFavorite>()

        val userFavorites = UserFavorites(
            tracks = allFavorites.filter { it.type == FavoriteType.TRACK },
            albums = allFavorites.filter { it.type == FavoriteType.ALBUM },
            artists = allFavorites.filter { it.type == FavoriteType.ARTIST }
        )

        return userFavorites
    }

    override suspend fun checkIfFavorite(
        userId: String,
        entityId: String
    ): UserFavorite? {
        return db.from(table).select {
            filter {
                and {
                    eq("uid", UUID.fromString(userId))
                    eq("entity_id", entityId)
                }
            }
        }.decodeSingleOrNull<UserFavorite>()
    }

}