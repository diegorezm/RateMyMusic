package com.diegorezm.ratemymusic.spotify_auth.data.network

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.spotify_auth.data.dto.SpotifyTokenDTO

interface RemoteSpotifyAuthDataSource {
    suspend fun refreshToken(refreshToken: String): Result<SpotifyTokenDTO, DataError.Remote>
    suspend fun requestAccesToken(code: String): Result<SpotifyTokenDTO, DataError.Remote>
}