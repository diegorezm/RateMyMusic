package com.diegorezm.ratemymusic.modules.spotify_auth.domain.use_cases

import android.util.Base64
import android.util.Log
import com.diegorezm.ratemymusic.modules.spotify_auth.data.remote.models.SpotifyTokenDTO
import com.diegorezm.ratemymusic.utils.getEnvRemote
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

        val spotifyClientId = getEnvRemote("SPOTIFY_CLIENT_ID").getOrElse {
            return Result.failure(
                Exception("Could not fetch the spotify client id.")
            )
        }
        val spotifySecretKey = getEnvRemote("SPOTIFY_SECRET_KEY").getOrElse {
            return Result.failure(
                Exception("Could not fetch the spotify secret key.")
            )
        }

        val credentials = "$spotifyClientId:$spotifySecretKey"
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
