package com.diegorezm.ratemymusic.modules.music.domain.repositories

import android.content.Context
import android.util.Log
import com.diegorezm.ratemymusic.modules.common.PublicException
import com.diegorezm.ratemymusic.modules.music.data.local.mappers.toDomain
import com.diegorezm.ratemymusic.modules.music.data.remote.api.SearchApi
import com.diegorezm.ratemymusic.modules.music.data.remote.api.SearchType
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.SearchRepository
import com.diegorezm.ratemymusic.modules.music.domain.models.AlbumSimple
import com.diegorezm.ratemymusic.modules.music.domain.models.Artist
import com.diegorezm.ratemymusic.modules.music.domain.models.PaginatedResult
import com.diegorezm.ratemymusic.modules.music.domain.models.SearchRequest
import com.diegorezm.ratemymusic.modules.music.domain.models.Track
import com.diegorezm.ratemymusic.utils.RetrofitClient

class SearchRemoteRepository(context: Context) : SearchRepository {
    private val retrofit = RetrofitClient.getRetrofit(context, "https://api.spotify.com/v1/")
    private val searchApi = retrofit.create(SearchApi::class.java)
    private val tag = "SearchRemoteRepository"

    override suspend fun searchByAlbum(request: SearchRequest): Result<PaginatedResult<AlbumSimple>> {
        return try {
            val response = searchApi.search(
                query = request.query,
                type = SearchType.ALBUM.name.lowercase(),
                limit = request.limit,
                offset = request.offset,
                authToken = "Bearer ${request.spotifyAuthToken}"
            )
            if (response.albums != null) {
                Result.success(response.albums.toDomain { it.toDomain() })
            } else {
                Result.failure(Exception("No albums found"))
            }
        } catch (e: Exception) {
            Log.e(tag, "Error searching by album", e)
            Result.failure(PublicException("Error searching by album."))
        }
    }

    override suspend fun searchByArtist(request: SearchRequest): Result<PaginatedResult<Artist>> {
        return try {
            val response = searchApi.search(
                query = request.query,
                type = SearchType.ARTIST.name.lowercase(),
                limit = request.limit,
                offset = request.offset,
                authToken = "Bearer ${request.spotifyAuthToken}"
            )
            if (response.artists != null) {
                Result.success(response.artists.toDomain { it.toDomain() })
            } else {
                Result.failure(Exception("No artists found"))
            }
        } catch (e: Exception) {
            Log.e(tag, "Error searching by artist", e)
            Result.failure(PublicException("Error searching by artist."))
        }
    }

    override suspend fun searchByTrack(request: SearchRequest): Result<PaginatedResult<Track>> {
        return try {
            val response = searchApi.search(
                query = request.query,
                type = SearchType.TRACK.name.lowercase(),
                limit = request.limit,
                offset = request.offset,
                authToken = "Bearer ${request.spotifyAuthToken}"
            )
            if (response.tracks != null) {
                Result.success(response.tracks.toDomain { it.toDomain() })
            } else {
                Result.failure(Exception("No artists found"))
            }
        } catch (e: Exception) {
            Log.e(tag, "Error searching by tracks", e)
            Result.failure(PublicException("Error searching by artist."))
        }
    }
}