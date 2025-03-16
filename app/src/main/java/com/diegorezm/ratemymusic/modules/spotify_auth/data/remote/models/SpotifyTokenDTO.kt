package com.diegorezm.ratemymusic.modules.spotify_auth.data.remote.models

import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.entities.SpotifyTokenEntity
import com.google.gson.annotations.SerializedName

data class SpotifyTokenDTO(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Long,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("scope") val scope: String
)

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