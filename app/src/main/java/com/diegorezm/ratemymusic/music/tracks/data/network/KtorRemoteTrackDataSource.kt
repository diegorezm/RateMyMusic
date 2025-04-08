package com.diegorezm.ratemymusic.music.tracks.data.network

import com.diegorezm.ratemymusic.core.data.safeCall
import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackDTO
import com.diegorezm.ratemymusic.spotify_auth.domain.SpotifyTokenRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders

class KtorRemoteTrackDataSource(
    private val httpClient: HttpClient,
    private val spotifyTokenRepository: SpotifyTokenRepository
) : RemoteTrackDataSource {
    private val url = "https://api.spotify.com/v1/tracks"
    override suspend fun getTracktById(id: String): Result<TrackDTO, DataError.Remote> {
        val tokenResult = spotifyTokenRepository.getValidToken()
        val reqUrl = "$url/$id"
        return when (tokenResult) {

            is Result.Success<String?> -> {
                safeCall {
                    httpClient.get(reqUrl) {
                        header(HttpHeaders.Authorization, "Bearer ${tokenResult.data}")
                    }
                }
            }

            is Result.Error<DataError.Remote> -> {
                Result.Error(DataError.Remote.UNAUTHORIZED)
            }

        }
    }

    override suspend fun getTracksByIds(ids: List<String>): Result<List<TrackDTO>, DataError.Remote> {
        val tokenResult = spotifyTokenRepository.getValidToken()
        val req = ids.joinToString(",")
        return when (tokenResult) {
            is Result.Success<String?> -> {
                safeCall {
                    httpClient.get(url) {
                        parameter("ids", req)
                        header(HttpHeaders.Authorization, "Bearer ${tokenResult.data}")
                    }
                }
            }

            is Result.Error<DataError.Remote> -> {
                Result.Error(DataError.Remote.UNAUTHORIZED)
            }
        }
    }
}