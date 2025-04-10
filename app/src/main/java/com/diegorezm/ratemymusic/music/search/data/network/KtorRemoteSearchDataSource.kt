package com.diegorezm.ratemymusic.music.search.data.network

import com.diegorezm.ratemymusic.core.data.safeCall
import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.music.albums.data.dto.PaginatedAlbumDTO
import com.diegorezm.ratemymusic.music.artists.data.dto.PaginatedArtistDTO
import com.diegorezm.ratemymusic.music.shared.dto.PaginationDTO
import com.diegorezm.ratemymusic.music.tracks.data.dto.PaginatedTrackDTO
import com.diegorezm.ratemymusic.spotify_auth.domain.SpotifyTokenRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders

class KtorRemoteSearchDataSource(
    private val client: HttpClient,
    private val spotifyTokenRepository: SpotifyTokenRepository
) : RemoteSearchDataSource {
    private val url = "https://api.spotify.com/v1/search"

    override suspend fun searchByAlbum(
        query: String,
        pagination: PaginationDTO
    ): Result<PaginatedAlbumDTO, DataError.Remote> {
        val token = spotifyTokenRepository.getValidToken()
        return when (token) {
            is Result.Success<String?> -> {
                safeCall {
                    client.get(url) {
                        parameter("q", query)
                        parameter("type", "album")
                        parameter("limit", pagination.limit)
                        parameter("offset", pagination.offset)
                        header(HttpHeaders.Authorization, "Bearer ${token.data}")
                    }
                }
            }

            is Result.Error<DataError.Remote> -> Result.Error(token.error)
        }
    }

    override suspend fun searchByTrack(
        query: String,
        pagination: PaginationDTO
    ): Result<PaginatedTrackDTO, DataError.Remote> {
        val token = spotifyTokenRepository.getValidToken()
        return when (token) {
            is Result.Success<String?> -> {
                safeCall {
                    client.get(url) {
                        parameter("q", query)
                        parameter("type", "track")
                        parameter("limit", pagination.limit)
                        parameter("offset", pagination.offset)
                        header(HttpHeaders.Authorization, "Bearer ${token.data}")
                    }
                }
            }

            is Result.Error<DataError.Remote> -> Result.Error(token.error)
        }
    }

    override suspend fun searchByArtist(
        query: String,
        pagination: PaginationDTO
    ): Result<PaginatedArtistDTO, DataError.Remote> {
        val token = spotifyTokenRepository.getValidToken()
        return when (token) {
            is Result.Success<String?> -> {
                safeCall {
                    client.get(url) {
                        parameter("q", query)
                        parameter("type", "artist")
                        parameter("limit", pagination.limit)
                        parameter("offset", pagination.offset)
                        header(HttpHeaders.Authorization, "Bearer ${token.data}")
                    }
                }
            }

            is Result.Error<DataError.Remote> -> Result.Error(token.error)
        }
    }

}