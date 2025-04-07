package com.diegorezm.ratemymusic.spotify_auth.data.network

import android.util.Base64
import com.diegorezm.ratemymusic.BuildConfig
import com.diegorezm.ratemymusic.core.data.safeCall
import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.spotify_auth.data.dto.SpotifyTokenDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import io.ktor.http.headers


class KtorRemoteSpotifyAuthDataSource(
    private val httpClient: HttpClient
) : RemoteSpotifyAuthDataSource {
    override suspend fun refreshToken(refreshToken: String): Result<SpotifyTokenDTO, DataError.Remote> {
        val url = "https://accounts.spotify.com/api/token"
        val spotifyClientId = BuildConfig.SPOTIFY_CLIENT_ID
        val spotifyClientSecret = BuildConfig.SPOTIFY_SECRET_KEY

        val credentials = "$spotifyClientId:$spotifyClientSecret"

        val basicAuth = "Basic " + Base64.encodeToString(
            credentials.toByteArray(),
            Base64.NO_WRAP
        )

        return safeCall {
            val response = httpClient.submitForm(
                url = url,
                formParameters = Parameters.build {
                    append("grant_type", "refresh_token")
                    append("refresh_token", refreshToken)
                },
                block = {
                    headers {
                        append("Authorization", basicAuth)
                    }
                }
            )

            if (response.status.value != 200) {
                if (response.status.value == 400) {
                    return Result.Error(DataError.Remote.BAD_REQUEST)
                }
                if (response.status.value == 401) {
                    return Result.Error(DataError.Remote.UNAUTHORIZED)
                }
                if (response.status.value == 403) {
                    return Result.Error(DataError.Remote.FORBIDDEN)
                }
                return Result.Error(DataError.Remote.UNKNOWN)
            }

            val body = response.body<SpotifyTokenDTO?>()
            if (body == null) {
                return Result.Error(DataError.Remote.SERIALIZATION)
            }
            return Result.Success(body)

        }
    }

    override suspend fun requestAccesToken(code: String): Result<SpotifyTokenDTO, DataError.Remote> {
        val redirectURI = "com.diegorezm.ratemymusic://callback"
        val requestURL = "https://accounts.spotify.com/api/token"

        val spotifyClientId = BuildConfig.SPOTIFY_CLIENT_ID
        val spotifyClientSecret = BuildConfig.SPOTIFY_SECRET_KEY
        val credentials = "$spotifyClientId:$spotifyClientSecret"
        val basicAuth = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        return safeCall {
            val response = httpClient.submitForm(
                url = requestURL,
                formParameters = Parameters.build {
                    append("grant_type", "authorization_code")
                    append("code", code)
                    append("redirect_uri", redirectURI)
                },
                block = {
                    headers {
                        append("Authorization", basicAuth)
                    }
                }
            )
            if (response.status.value != 200) {
                if (response.status.value == 400) {
                    return Result.Error(DataError.Remote.BAD_REQUEST)
                }
                if (response.status.value == 401) {
                    return Result.Error(DataError.Remote.UNAUTHORIZED)
                }
                if (response.status.value == 403) {
                    return Result.Error(DataError.Remote.FORBIDDEN)
                }
                return Result.Error(DataError.Remote.UNKNOWN)
            }
            val body = response.body<SpotifyTokenDTO?>()
            if (body == null) {
                return Result.Error(DataError.Remote.SERIALIZATION)
            }
            return Result.Success(body)

        }
    }
}