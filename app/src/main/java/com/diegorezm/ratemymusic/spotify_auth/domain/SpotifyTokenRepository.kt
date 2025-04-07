package com.diegorezm.ratemymusic.spotify_auth.domain

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.EmptyResult
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.spotify_auth.data.dto.SpotifyTokenDTO

interface SpotifyTokenRepository {
    suspend fun saveToken(tokenDTO: SpotifyTokenDTO): EmptyResult<DataError.Local>
    suspend fun requestAccesToken(code: String): EmptyResult<DataError.Remote>
    suspend fun refreshToken(): Result<String, DataError.Remote>
    suspend fun getValidToken(): Result<String?, DataError.Remote>
    suspend fun getToken(): Result<String, DataError.Local>
    suspend fun clearToken(): EmptyResult<DataError.Local>
    suspend fun isTokenExpired(): Boolean
}