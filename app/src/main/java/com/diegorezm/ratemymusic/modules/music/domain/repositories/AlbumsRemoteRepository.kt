package com.diegorezm.ratemymusic.modules.music.domain.repositories

import android.content.Context
import android.util.Log
import com.diegorezm.ratemymusic.modules.music.data.local.mappers.toDomain
import com.diegorezm.ratemymusic.modules.music.data.remote.api.AlbumsApi
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.AlbumsRepository
import com.diegorezm.ratemymusic.modules.music.domain.models.Album
import com.diegorezm.ratemymusic.modules.music.domain.models.PaginatedResult
import com.diegorezm.ratemymusic.modules.music.domain.models.TrackSimple
import com.diegorezm.ratemymusic.utils.RetrofitClient

class AlbumsRemoteRepository(
    context: Context
) : AlbumsRepository {
    private val retrofit = RetrofitClient.getRetrofit(context, "https://api.spotify.com/v1/")
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

    override suspend fun getByIds(
        albumIds: List<String>,
        spotifyAuthToken: String?
    ): Result<List<Album>> {
        if (spotifyAuthToken == null) return Result.failure(Exception("Please provide a valid Spotify auth token."))
        val req = albumIds.joinToString(",")
        return try {
            val response = albumsApi.getSeveralAlbums(req, "Bearer $spotifyAuthToken")
            val albums = response.albums.map {
                try {
                    it.toDomain()
                } catch (e: Exception) {
                    Log.e(tag, "Error parsing album with id: ${it.id}.\n $it", e)
                    return@map null
                }
            }.filterNotNull()

            Result.success(albums)
        } catch (e: Exception) {
            Log.e(tag, "Error getting albums.", e)
            Result.failure(e)
        }
    }

    override suspend fun getTracks(
        albumId: String,
        spotifyAuthToken: String?
    ): Result<PaginatedResult<TrackSimple>> {
        if (spotifyAuthToken == null) return Result.failure(Exception("Please provide a valid Spotify auth token."))
        return try {
            val tracks = albumsApi.getAlbumTracks(albumId, "Bearer $spotifyAuthToken")
            Result.success(tracks.toDomain {
                it.toDomain()
            })
        } catch (e: Exception) {
            Log.e(tag, "Error getting album tracks: $albumId", e)
            Result.failure(e)
        }
    }

    override suspend fun getNewReleases(spotifyAuthToken: String?): Result<List<Album>> {
        if (spotifyAuthToken == null) return Result.failure(Exception("Please provide a valid Spotify auth token."))
        return try {
            val albums = albumsApi.getNewReleases("Bearer $spotifyAuthToken")
            Result.success(albums.map { it.toDomain() })
        } catch (e: Exception) {
            Log.e(tag, "Error getting new releases", e)
            Result.failure(e)
        }
    }
}