package com.diegorezm.ratemymusic.modules.favorites.domain.repositories

import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteDTO
import com.diegorezm.ratemymusic.modules.favorites.data.models.FavoriteType
import com.diegorezm.ratemymusic.modules.favorites.data.repositories.FavoritesRepository
import com.diegorezm.ratemymusic.modules.favorites.domain.models.UserFavorites
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FavoritesRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : FavoritesRepository {
    override suspend fun addFavoriteTrack(
        userId: String,
        trackId: String
    ): Result<Unit> {
        return try {
            val userFavoritesRef = db.collection("user_favorites").document(userId)
            val snapshot = userFavoritesRef.get().await()

            if (!snapshot.exists() || !snapshot.contains("tracks")) {
                userFavoritesRef.set(mapOf("tracks" to listOf(trackId)), SetOptions.merge()).await()
            } else {
                val tracks = snapshot.get("tracks") as? List<*>
                if (tracks?.contains(trackId) == true) return Result.success(Unit)

                userFavoritesRef.update("albums", FieldValue.arrayUnion(trackId)).await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addFavoriteAlbum(
        userId: String,
        albumId: String
    ): Result<Unit> {
        return try {
            val userFavoritesRef = db.collection("user_favorites").document(userId)
            val snapshot = userFavoritesRef.get().await()

            if (!snapshot.exists() || !snapshot.contains("albums")) {
                userFavoritesRef.set(mapOf("albums" to listOf(albumId)), SetOptions.merge()).await()
            } else {
                val albums = snapshot.get("albums") as? List<*>
                if (albums?.contains(albumId) == true) return Result.success(Unit)
                userFavoritesRef.update("albums", FieldValue.arrayUnion(albumId)).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addFavoriteArtist(
        userId: String,
        artistId: String
    ): Result<Unit> {
        return try {
            val userFavoritesRef = db.collection("user_favorites").document(userId)
            val snapshot = userFavoritesRef.get().await()

            if (!snapshot.exists() || !snapshot.contains("artists")) {
                userFavoritesRef.set(mapOf("artists" to listOf(artistId)), SetOptions.merge())
                    .await()
            } else {

                val artists = snapshot.get("artists") as? List<*>
                if (artists?.contains(artistId) == true) return Result.success(Unit)

                userFavoritesRef.update("artists", FieldValue.arrayUnion(artistId)).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFavoriteTrack(
        userId: String,
        songId: String
    ): Result<Unit> {
        return try {
            val userFavoritesRef = db.collection("user_favorites").document(userId)
            val snapshot = userFavoritesRef.get().await()

            if (!snapshot.exists() || !snapshot.contains("tracks")) {
                return Result.failure(Exception("User favorites not found."))
            } else {
                userFavoritesRef.update("tracks", FieldValue.arrayRemove(songId)).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFavoriteAlbum(
        userId: String,
        albumId: String
    ): Result<Unit> {
        return try {
            val userFavoritesRef = db.collection("user_favorites").document(userId)
            val snapshot = userFavoritesRef.get().await()

            if (!snapshot.exists() || !snapshot.contains("albums")) {
                return Result.failure(Exception("User favorites not found."))
            } else {
                userFavoritesRef.update("albums", FieldValue.arrayRemove(albumId)).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFavoriteArtist(
        userId: String,
        artistId: String
    ): Result<Unit> {
        return try {
            val userFavoritesRef = db.collection("user_favorites").document(userId)
            val snapshot = userFavoritesRef.get().await()

            if (!snapshot.exists() || !snapshot.contains("artists")) {
                return Result.failure(Exception("User favorites not found."))
            } else {
                userFavoritesRef.update("artists", FieldValue.arrayRemove(artistId)).await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserFavorites(userId: String): Result<UserFavorites> {
        return try {
            val userFavoritesRef = db.collection("user_favorites").document(userId).get().await()

            if (!userFavoritesRef.exists()) {
                return Result.failure(Exception("User favorites not found."))
            }

            val res = userFavoritesRef.toObject(UserFavorites::class.java)
            if (res == null) {
                return Result.failure(Exception("User favorites not found."))
            }

            Result.success(res)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkIfFavorite(
        favoriteDTO: FavoriteDTO
    ): Result<Boolean> {
        return try {
            val userFavoritesRef = db.collection("user_favorites").document(favoriteDTO.uid)
            val snapshot = userFavoritesRef.get().await()

            if (!snapshot.exists()) return Result.success(false)

            when (favoriteDTO.type) {
                FavoriteType.TRACK -> {
                    val tracks = snapshot.get("tracks") as? List<*>
                    return Result.success(tracks?.contains(favoriteDTO.favoriteId) == true)
                }

                FavoriteType.ALBUM -> {
                    val albums = snapshot.get("albums") as? List<*>
                    return Result.success(albums?.contains(favoriteDTO.favoriteId) == true)
                }

                FavoriteType.ARTIST -> {
                    val artists = snapshot.get("artists") as? List<*>
                    return Result.success(artists?.contains(favoriteDTO.favoriteId) == true)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}