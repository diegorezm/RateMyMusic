package com.diegorezm.ratemymusic.modules.spotify_auth.domain.use_cases

import android.util.Log
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories.SpotifyTokenRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.data.remote.models.SpotifyTokenDTO
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

suspend fun refreshSpotifyTokenUseCase(repository: SpotifyTokenRepository): Result<String> {
    val refreshToken = repository.getToken()?.refreshToken
        ?: return Result.failure(Exception("No refresh token found"))
    return try {
        val url = "https://accounts.spotify.com/api/token"
        val spotifyClientId = "9bdf557ef4d84de3a1c09eb5440e9a71"

        val formBody = FormBody.Builder()
            .add("grant_type", "refresh_token")
            .add("refresh_token", refreshToken)
            .add("client_id", spotifyClientId)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
            val json = org.json.JSONObject(responseBody)

            val newAccessToken = json.getString("access_token")
            val newRefreshToken = json.optString("refresh_token", refreshToken)
            val scope = json.optString("scope", "")
            val expiresIn = json.optLong("expires_in", 0)

            val dto = SpotifyTokenDTO(
                accessToken = newAccessToken,
                refreshToken = newRefreshToken,
                tokenType = "\"Bearer\"",
                scope = scope,
                expiresIn = expiresIn
            )

            repository.saveToken(dto)
            Result.success(newAccessToken)
        } else {
            Result.failure(Exception("Failed to refresh token: ${response.code}"))

        }
    } catch (e: Exception) {
        Log.e("refreshSpotifyTokenUseCase", "Error: ${e.message}", e)
        Result.failure(e)
    }
}
