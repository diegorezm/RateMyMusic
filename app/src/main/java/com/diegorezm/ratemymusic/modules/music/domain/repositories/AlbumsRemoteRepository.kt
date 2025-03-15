package com.diegorezm.ratemymusic.modules.music.domain.repositories

import android.content.Context
import android.util.Log
import com.diegorezm.ratemymusic.modules.music.data.remote.api.AlbumsApi
import com.diegorezm.ratemymusic.modules.music.data.remote.mappers.toDomain
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.AlbumsRepository
import com.diegorezm.ratemymusic.modules.music.domain.models.Album
import com.diegorezm.ratemymusic.utils.RetrofitClient

class AlbumsRemoteRepository(
    context: Context
) : AlbumsRepository {
    private val retrofit = RetrofitClient.getRetrofit(context, "https://api.spotify.com/v1/albums/")
    private val albumsApi = retrofit.create(AlbumsApi::class.java)
    private val tag = "AlbumsRemoteRepository"

    override suspend fun getById(albumId: String, spotifyAuthToken: String?): Result<Album> {
        if (spotifyAuthToken == null) return Result.failure(Exception("Please provide a valid Spotify auth token."))
        return try {
            val album = albumsApi.getAlbumById(albumId, "Bearer $spotifyAuthToken")
            Result.success(album.toDomain())
        } catch (e: Exception) {
            Log.e(tag, "Error getting album by id: $albumId", e)
            Result.failure(e)
        }
    }

    override suspend fun getTracks(
        albumId: String,
        spotifyAuthToken: String?
    ): Result<List<Album>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNewReleases(spotifyAuthToken: String?): Result<List<Album>> {
        TODO("Not yet implemented")
    }


}