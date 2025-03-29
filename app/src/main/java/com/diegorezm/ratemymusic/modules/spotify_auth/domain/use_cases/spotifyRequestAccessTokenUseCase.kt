package com.diegorezm.ratemymusic.modules.spotify_auth.domain.use_cases

import android.util.Base64
import android.util.Log
import com.diegorezm.ratemymusic.BuildConfig
import com.diegorezm.ratemymusic.modules.spotify_auth.data.remote.models.SpotifyTokenDTO
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

suspend fun spotifyRequestAccessTokenUseCase(
    code: String
): Result<SpotifyTokenDTO> {
    return try {
        val redirectURI = "com.diegorezm.ratemymusic://callback"
        val requestURL = "https://accounts.spotify.com/api/token"

        val spotifyClientId = BuildConfig.SPOTIFY_CLIENT_ID

        val spotifyClientSecret = BuildConfig.SPOTIFY_SECRET_KEY

        Log.i("refreshSpotifyTokenUseCase", "Refreshing token with refresh token: $spotifyClientId")


        val credentials = "$spotifyClientId:$spotifyClientSecret"
        val basicAuth = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        val client = OkHttpClient()

        val requestBody = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("code", code)
            .add("redirect_uri", redirectURI)
            .build()

        val request = Request.Builder()
            .url(requestURL)
            .post(requestBody)
            .addHeader("Authorization", basicAuth)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        if (response.isSuccessful && responseBody != null) {
            val json = Gson().fromJson(responseBody, SpotifyTokenDTO::class.java)
            if (json != null) {
                return Result.success(json)
            }
            return Result.failure(Exception("Spotify did not return a valid token."))
        }
        Log.e(
            "SpotifyToken",
            "Something went wrong with the Spotify request. Response Body:\n $responseBody\n"
        )
        return Result.failure(Exception("There was a problem contacting Spotify."))
    } catch (e: Exception) {
        return Result.failure(e)
    }
}
