package com.diegorezm.ratemymusic.music.artists.data.network

import com.diegorezm.ratemymusic.core.data.safeCall
import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.modules.music.data.remote.models.ArtistDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackBulkDTO
import com.diegorezm.ratemymusic.music.albums.data.dto.PaginatedAlbumDTO
import com.diegorezm.ratemymusic.music.artists.data.dto.ArtistBulkDTO
import com.diegorezm.ratemymusic.music.shared.dto.PaginationDTO
import com.diegorezm.ratemymusic.spotify_auth.domain.SpotifyTokenRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders

class KtorRemoteArtistDataSource(
    private val httpClient: HttpClient,
    private val spotifyTokenRepository: SpotifyTokenRepository
) : RemoteArtistDataSource {
    private val url = "https://api.spotify.com/v1/artists"
    override suspend fun getArtistById(id: String): Result<ArtistDTO, DataError.Remote> {
        val token = spotifyTokenRepository.getValidToken()
        return when (token) {
            is Result.Error<*> -> {
                Result.Error(DataError.Remote.UNAUTHORIZED)
            }

            is Result.Success<*> -> {
                safeCall {
                    httpClient.get("$url/$id") {
                        header(HttpHeaders.Authorization, "Bearer ${token.data}")
                    }
                }
            }
        }
    }

    override suspend fun getArtistsByIds(ids: List<String>): Result<ArtistBulkDTO, DataError.Remote> {
        val token = spotifyTokenRepository.getValidToken()
        val req = ids.joinToString(",")

        return when (token) {
            is Result.Error<*> -> {
                Result.Error(DataError.Remote.UNAUTHORIZED)
            }

            is Result.Success<*> -> {
                safeCall {
                    httpClient.get(url) {
                        header(HttpHeaders.Authorization, "Bearer ${token.data}")
                        parameter("ids", req)
                    }
                }
            }
        }
    }

    override suspend fun getArtistTopTracks(id: String): Result<TrackBulkDTO, DataError.Remote> {
        val token = spotifyTokenRepository.getValidToken()

        return when (token) {
            is Result.Error<*> -> {
                Result.Error(DataError.Remote.UNAUTHORIZED)
            }

            is Result.Success<*> -> {
                safeCall {
                    httpClient.get("$url/$id/top-tracks") {
                        header(HttpHeaders.Authorization, "Bearer ${token.data}")
                    }
                }
            }
        }
    }

    override suspend fun getArtistAlbums(
        id: String,
        pagination: PaginationDTO
    ): Result<PaginatedAlbumDTO, DataError.Remote> {
        val token = spotifyTokenRepository.getValidToken()

        return when (token) {
            is Result.Error<*> -> {
                Result.Error(DataError.Remote.UNAUTHORIZED)
            }

            is Result.Success<*> -> {
                safeCall {
                    httpClient.get("$url/$id/albums") {
                        header(HttpHeaders.Authorization, "Bearer ${token.data}")
                        parameter("offset", pagination.offset)
                        parameter("limit", pagination.limit)
                    }
                }
            }
        }
    }
}