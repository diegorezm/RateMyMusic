package com.diegorezm.ratemymusic.music.albums.data.network

import android.util.Log
import com.diegorezm.ratemymusic.core.data.safeCall
import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.music.albums.data.dto.AlbumDTO
import com.diegorezm.ratemymusic.music.albums.data.dto.PaginatedAlbumDTO
import com.diegorezm.ratemymusic.spotify_auth.domain.SpotifyTokenRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders

class KtorRemoteAlbumRepository(
    private val httpClient: HttpClient,
    private val spotifyTokenRepository: SpotifyTokenRepository
) : RemoteAlbumRepository {
    private val url = "https://api.spotify.com/v1/albums"

    override suspend fun getAlbumById(id: String): Result<AlbumDTO, DataError.Remote> {
        val requestUrl = "$url/$id"
        val token = spotifyTokenRepository.getValidToken()

        return when (token) {
            is Result.Success<String?> -> {
                return safeCall {
                    httpClient.get(requestUrl) {
                        header(HttpHeaders.Authorization, "Bearer ${token.data}")
                    }
                }
            }

            is Result.Error<DataError.Remote> -> {
                Result.Error(DataError.Remote.UNAUTHORIZED)
            }
        }
    }

    override suspend fun getAlbumsByIds(ids: List<String>): Result<List<AlbumDTO>, DataError.Remote> {
        val token = spotifyTokenRepository.getValidToken()
        val req = ids.joinToString(",")
        return when (token) {
            is Result.Success<String?> -> {
                return safeCall {
                    httpClient.get(url) {
                        parameter("ids", req)
                        header(HttpHeaders.Authorization, "Bearer ${token.data}")
                    }
                }
            }

            is Result.Error<DataError.Remote> -> {
                return Result.Error(DataError.Remote.UNAUTHORIZED)
            }
        }
    }

    override suspend fun getNewReleases(): Result<PaginatedAlbumDTO, DataError.Remote> {
        val token = spotifyTokenRepository.getValidToken()
        val reqUrl =
            "https://api.spotify.com/v1/browse/new-releases"

        return when (token) {
            is Result.Success -> {
                val t = token.data
                Log.i("KtorRemoteAlbumRepository", "Token: ${t}")
                return safeCall<PaginatedAlbumDTO> {
                    httpClient.get(reqUrl) {
                        header(HttpHeaders.Authorization, "Bearer $t")
                    }
                }
            }

            is Result.Error -> {
                Result.Error(DataError.Remote.UNAUTHORIZED)
            }
        }
    }

}