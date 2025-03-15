package com.diegorezm.ratemymusic.modules.spotify_auth.domain.use_cases

import android.util.Log
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories.SpotifyTokenRepository

suspend fun getValidSpotifyAccessToken(repository: SpotifyTokenRepository): Result<String?> {
    return try {
        if (repository.isTokenExpired()) {
            val newToken = refreshSpotifyTokenUseCase(repository).getOrNull()
            Result.success(newToken?.accessToken)
        } else {
            Result.success(repository.getToken()?.accessToken)
        }
    } catch (e: Exception) {
        Log.e("SpotifyToken", "Error getting valid token", e)
        Result.failure(e)
    }

}