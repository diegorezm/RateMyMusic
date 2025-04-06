package com.diegorezm.ratemymusic.spotify_auth.domain

import com.diegorezm.ratemymusic.spotify_auth.data.database.SpotifyTokenEntity
import com.diegorezm.ratemymusic.spotify_auth.data.dto.SpotifyTokenDTO

interface SpotifyTokenRepository {
    suspend fun saveToken(tokenDTO: SpotifyTokenDTO)
    suspend fun getToken(): SpotifyTokenEntity?
    suspend fun clearToken()
    suspend fun isTokenExpired(): Boolean
}