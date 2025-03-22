package com.diegorezm.ratemymusic.modules.music.domain.repositories

import android.content.Context
import android.util.Log
import com.diegorezm.ratemymusic.modules.music.data.local.mappers.toDomain
import com.diegorezm.ratemymusic.modules.music.data.remote.api.TracksApi
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.TracksRepository
import com.diegorezm.ratemymusic.modules.music.domain.models.Track
import com.diegorezm.ratemymusic.utils.RetrofitClient

class TracksRemoteRepository(
    context: Context
) : TracksRepository {
    private val retrofit = RetrofitClient.getRetrofit(context, "https://api.spotify.com/v1/")
    private val tracksApi = retrofit.create(TracksApi::class.java)

    override suspend fun getById(id: String, spotifyAuthToken: String?): Result<Track> {
        if (spotifyAuthToken.isNullOrEmpty()) return Result.failure(Exception("Please provide a valid Spotify auth token."))
        return try {
            val track = tracksApi.getTrackById(id, "Bearer $spotifyAuthToken")
            Result.success(track.toDomain())
        } catch (e: Exception) {
            Log.e("TracksRemoteRepository", "Error getting track by id: $id", e)
            Result.failure(e)
        }
    }

    override suspend fun getByIds(
        ids: List<String>,
        spotifyAuthToken: String?
    ): Result<List<Track>> {
        if (spotifyAuthToken.isNullOrEmpty()) return Result.failure(Exception("Please provide a valid Spotify auth token."))
        return try {
            val response = tracksApi.getTrackByIds(ids, "Bearer $spotifyAuthToken")
            val tracks = response.tracks.map { it.toDomain() }
            Result.success(tracks)
        } catch (e: Exception) {
            Log.e("TracksRemoteRepository", "Error getting tracks", e)
            Result.failure(e)
        }
    }
}