package com.diegorezm.ratemymusic.spotify_auth.data.repositories

import android.util.Log
import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.EmptyResult
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.spotify_auth.data.database.SpotifyTokenDAO
import com.diegorezm.ratemymusic.spotify_auth.data.dto.SpotifyTokenDTO
import com.diegorezm.ratemymusic.spotify_auth.data.mappers.toEntity
import com.diegorezm.ratemymusic.spotify_auth.data.network.RemoteSpotifyAuthDataSource
import com.diegorezm.ratemymusic.spotify_auth.domain.SpotifyTokenRepository

class DefaultSpotifyTokenRepository(
    private val spotifyTokenDao: SpotifyTokenDAO,
    private val remoteSpotifyAuthDataSource: RemoteSpotifyAuthDataSource
) : SpotifyTokenRepository {
    override suspend fun saveToken(tokenDTO: SpotifyTokenDTO): EmptyResult<DataError.Local> {
        return try {
            spotifyTokenDao.insertToken(tokenDTO.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e("DefaultSpotifyTokenRepository", "saveToken: ${e.message}")
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun requestAccesToken(code: String): EmptyResult<DataError.Remote> {
        val result = remoteSpotifyAuthDataSource.requestAccesToken(code)
        if (result is Result.Success) {
            spotifyTokenDao.insertToken(result.data.toEntity())
            return Result.Success(Unit)
        }
        if (result is Result.Error) {
            return Result.Error(result.error)
        }
        return Result.Error(DataError.Remote.UNKNOWN)
    }

    override suspend fun refreshToken(): Result<String, DataError.Remote> {
        val token = spotifyTokenDao.getToken() ?: return Result.Error(DataError.Remote.UNAUTHORIZED)
        val refreshTokenResult =
            remoteSpotifyAuthDataSource.refreshToken(token.refreshToken)

        return if (refreshTokenResult is Result.Success) {
            spotifyTokenDao.insertToken(refreshTokenResult.data.toEntity())
            Result.Success(refreshTokenResult.data.accessToken)
        } else if (refreshTokenResult is Result.Error) {
            val error = refreshTokenResult.error
            Result.Error(error)
        } else {
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getValidToken(): Result<String?, DataError.Remote> {
        return try {
            val token = spotifyTokenDao.getToken()
            if (token == null) {
                return Result.Error(DataError.Remote.UNAUTHORIZED)
            }

            val currentTime = System.currentTimeMillis()

            if (currentTime < token.expiresIn) {
                return Result.Success(token.accessToken)
            }

            refreshToken().onSuccess {
                return Result.Success(it)
            }.onError {
                return Result.Error(it)
            }
        } catch (e: Exception) {
            Log.e("DefaultSpotifyTokenRepository", "getValidToken: ${e.message}")
            return Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getToken(): Result<String, DataError.Local> {
        return try {
            val token = spotifyTokenDao.getToken()
            if (token == null) {
                return Result.Error(DataError.Local.NOT_FOUND)
            }
            return Result.Success(token.accessToken)
        } catch (e: Exception) {
            Log.e("DefaultSpotifyTokenRepository", "getToken: ${e.message}")
            return Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun clearToken(): EmptyResult<DataError.Local> {
        return try {
            spotifyTokenDao.clearToken()
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e("DefaultSpotifyTokenRepository", "clearToken: ${e.message}")
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun isTokenExpired(): Boolean {
        return try {
            val tokenResult = spotifyTokenDao.getToken()
            if (tokenResult == null) return true
            val currentTime = System.currentTimeMillis()
            return currentTime >= tokenResult.expiresIn
        } catch (e: Exception) {
            Log.e("DefaultSpotifyTokenRepository", "isTokenExpired: ${e.message}", e)
            return true
        }
    }
}