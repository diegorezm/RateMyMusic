package com.diegorezm.ratemymusic.modules.spotify_auth.domain.use_cases

import android.util.Base64
import android.util.Log
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories.SpotifyTokenRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.data.remote.models.SpotifyTokenDTO
import com.diegorezm.ratemymusic.utils.getEnvRemote
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

suspend fun refreshSpotifyTokenUseCase(repository: SpotifyTokenRepository): Result<SpotifyTokenDTO> {
    return try {
        val tokenEntity =
            repository.getToken() ?: return Result.failure(Exception("No token found"))
        val refreshToken = tokenEntity.refreshToken

        val clientId = getEnvRemote("SPOTIFY_CLIENT_ID").getOrElse {
            return Result.failure(Exception("Missing Client ID"))
        }
        val clientSecret = getEnvRemote("SPOTIFY_SECRET_KEY").getOrElse {
            return Result.failure(Exception("Missing Secret Key"))
        }

        val credentials = "$clientId:$clientSecret"
        val basicAuth = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        val requestBody = FormBody.Builder()
            .add("grant_type", "refresh_token")
            .add("refresh_token", refreshToken)
            .build()

        val request = Request.Builder()
            .url("https://accounts.spotify.com/api/token")
            .post(requestBody)
            .addHeader("Authorization", basicAuth)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        if (response.isSuccessful && responseBody != null) {
            val newToken = Gson().fromJson(responseBody, SpotifyTokenDTO::class.java)
            repository.saveToken(newToken)
            return Result.success(newToken)
        }

        Log.e("SpotifyToken", "Failed to refresh token. Response: $responseBody")
        return Result.failure(Exception("Failed to refresh token"))

    } catch (e: Exception) {
        Log.e("SpotifyToken", "Error refreshing token", e)
        return Result.failure(e)
    }
}
