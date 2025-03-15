package com.diegorezm.ratemymusic.modules.spotify_auth.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spotify_tokens")
data class SpotifyTokenEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val refreshToken: String,
    val scope: String
)