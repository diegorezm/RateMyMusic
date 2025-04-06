package com.diegorezm.ratemymusic.spotify_auth.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spotify_token")
data class SpotifyTokenEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val refreshToken: String,
    val scope: String
)
