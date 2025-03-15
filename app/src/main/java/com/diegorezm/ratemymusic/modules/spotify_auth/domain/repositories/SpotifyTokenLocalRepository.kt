package com.diegorezm.ratemymusic.modules.spotify_auth.domain.repositories

import android.content.Context
import com.diegorezm.ratemymusic.data.AppDatabase
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.entities.SpotifyTokenEntity
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories.SpotifyTokenRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.data.remote.models.SpotifyTokenDTO
import com.diegorezm.ratemymusic.modules.spotify_auth.data.remote.models.toEntity

class SpotifyTokenLocalRepository(context: Context) : SpotifyTokenRepository {
    private val db = AppDatabase.getInstance(context)
    private val tokenDAO = db.spotifyTokenDao()

    override suspend fun saveToken(tokenDTO: SpotifyTokenDTO) {
        tokenDAO.insertToken(tokenDTO.toEntity())
    }

    override suspend fun getToken(): SpotifyTokenEntity? {
        return tokenDAO.getToken()
    }

    override suspend fun clearToken() {
        tokenDAO.clearToken()
    }

    override suspend fun isTokenExpired(): Boolean {
        val token = getToken() ?: return true
        return System.currentTimeMillis() > token.expiresIn
    }
}