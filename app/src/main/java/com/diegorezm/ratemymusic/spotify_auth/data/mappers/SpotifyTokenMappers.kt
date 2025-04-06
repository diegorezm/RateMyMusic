package com.diegorezm.ratemymusic.spotify_auth.data.mappers

import com.diegorezm.ratemymusic.spotify_auth.data.database.SpotifyTokenEntity
import com.diegorezm.ratemymusic.spotify_auth.data.dto.SpotifyTokenDTO

fun SpotifyTokenDTO.toEntity(): SpotifyTokenEntity {
    val expiresIn = System.currentTimeMillis() + (this.expiresIn * 1000)

    return SpotifyTokenEntity(
        accessToken = this.accessToken,
        tokenType = this.tokenType,
        expiresIn = expiresIn,
        refreshToken = this.refreshToken,
        scope = this.scope
    )

}