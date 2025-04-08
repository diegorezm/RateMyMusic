package com.diegorezm.ratemymusic.spotify_auth.data.network

import android.util.Base64
import com.diegorezm.ratemymusic.BuildConfig
import com.diegorezm.ratemymusic.core.data.safeCall
import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.spotify_auth.data.dto.SpotifyTokenDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters


class KtorRemoteSpotifyAuthDataSource(
    private val httpClient: HttpClient
) : RemoteSpotifyAuthDataSource {
    override suspend fun refreshToken(refreshToken: String): Result<SpotifyTokenDTO, DataError.Remote> {
        val url = "https://accounts.spotify.com/api/token"
        val spotifyClientId = BuildConfig.SPOTIFY_CLIENT_ID
        val spotifyClientSecret = BuildConfig.SPOTIFY_SECRET_KEY

        val credentials = "$spotifyClientId:$spotifyClientSecret"
        val basicAuth = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        return safeCall {
            httpClient.submitForm(
                url = url,
                formParameters = Parameters.build {
                    append("grant_type", "refresh_token")
                    append("refresh_token", refreshToken)
                },
            ) {
                header(HttpHeaders.Authorization, basicAuth)
            }

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
            httpClient.submitForm(
                url = requestURL,
                formParameters = Parameters.build {
                    append("grant_type", "authorization_code")
                    append("code", code)
                    append("redirect_uri", redirectURI)
                }
            ) {
                header(HttpHeaders.Authorization, basicAuth)
            }
        }
    }
}