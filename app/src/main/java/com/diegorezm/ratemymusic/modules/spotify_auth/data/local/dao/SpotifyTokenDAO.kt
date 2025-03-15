package com.diegorezm.ratemymusic.modules.spotify_auth.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.entities.SpotifyTokenEntity

@Dao
interface SpotifyTokenDAO {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertToken(token: SpotifyTokenEntity)

    @Query("SELECT * FROM spotify_tokens LIMIT 1")
    suspend fun getToken(): SpotifyTokenEntity?

    @Query("DELETE FROM spotify_tokens")
    suspend fun clearToken()
}