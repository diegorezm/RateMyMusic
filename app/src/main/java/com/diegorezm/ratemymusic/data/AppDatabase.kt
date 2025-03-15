package com.diegorezm.ratemymusic.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.dao.SpotifyTokenDAO
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.entities.SpotifyTokenEntity

@Database(entities = [SpotifyTokenEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun spotifyTokenDao(): SpotifyTokenDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}