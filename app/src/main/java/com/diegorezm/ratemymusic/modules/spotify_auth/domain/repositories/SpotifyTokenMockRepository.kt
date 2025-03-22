package com.diegorezm.ratemymusic.modules.spotify_auth.domain.repositories

import android.util.Log
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.entities.SpotifyTokenEntity
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories.SpotifyTokenRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.data.remote.models.SpotifyTokenDTO

class SpotifyTokenMockRepository : SpotifyTokenRepository {
    override suspend fun saveToken(tokenDTO: SpotifyTokenDTO) {
        Log.d("SpotifyTokenMockRepository", "Saving token: $tokenDTO")
    }

    override suspend fun getToken(): SpotifyTokenEntity? {
        return null
    }

    override suspend fun clearToken() {
        Log.d("SpotifyTokenMockRepository", "Clearing token")
    }

    override suspend fun isTokenExpired(): Boolean {
        return false
    }
}