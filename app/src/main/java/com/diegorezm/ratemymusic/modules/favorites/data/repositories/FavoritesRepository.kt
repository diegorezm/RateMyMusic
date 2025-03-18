package com.diegorezm.ratemymusic.modules.favorites.data.repositories

import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteDTO
import com.diegorezm.ratemymusic.modules.favorites.domain.models.UserFavorites

interface FavoritesRepository {
    suspend fun addFavoriteTrack(userId: String, trackId: String): Result<Unit>
    suspend fun addFavoriteAlbum(userId: String, albumId: String): Result<Unit>
    suspend fun addFavoriteArtist(userId: String, artistId: String): Result<Unit>

    suspend fun removeFavoriteTrack(userId: String, songId: String): Result<Unit>
    suspend fun removeFavoriteAlbum(userId: String, albumId: String): Result<Unit>
    suspend fun removeFavoriteArtist(userId: String, artistId: String): Result<Unit>

    suspend fun getUserFavorites(userId: String): Result<UserFavorites>
    suspend fun checkIfFavorite(favoriteDTO: FavoriteDTO): Result<Boolean>
}