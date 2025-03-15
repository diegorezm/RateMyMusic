package com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories

import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.entities.SpotifyTokenEntity
import com.diegorezm.ratemymusic.modules.spotify_auth.data.remote.models.SpotifyTokenDTO

interface SpotifyTokenRepository {
    suspend fun saveToken(tokenDTO: SpotifyTokenDTO)
    suspend fun getToken(): SpotifyTokenEntity?
    suspend fun clearToken()
    suspend fun isTokenExpired(): Boolean
}