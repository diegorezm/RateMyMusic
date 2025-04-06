package com.diegorezm.ratemymusic.spotify_auth.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SpotifyTokenEntity::class], version = 1, exportSchema = false)
abstract class SpotifyTokenDatabase : RoomDatabase() {
    abstract val spotifyTokenDao: SpotifyTokenDAO

    companion object {
        const val DATABASE_NAME = "spotify_tokens_db"
    }
}