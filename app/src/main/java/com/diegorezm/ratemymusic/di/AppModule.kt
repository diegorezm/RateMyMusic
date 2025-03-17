package com.diegorezm.ratemymusic.di

import android.content.Context
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.AlbumsRepository
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.TracksRepository
import com.diegorezm.ratemymusic.modules.music.domain.repositories.AlbumsRemoteRepository
import com.diegorezm.ratemymusic.modules.music.domain.repositories.TracksRemoteRepository
import com.diegorezm.ratemymusic.modules.profiles.data_access.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.data_access.ProfileRepositoryImpl
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories.SpotifyTokenRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.domain.repositories.SpotifyTokenLocalRepository
import com.google.firebase.firestore.FirebaseFirestore

interface AppModule {
    val db: FirebaseFirestore

    val profileRepository: ProfileRepository
    val spotifyTokenRepository: SpotifyTokenRepository
    val albumsRepository: AlbumsRepository
    val tracksRepository: TracksRepository
}

class AppModuleImpl(context: Context) : AppModule {

    override val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryImpl(db)
    }

    override val spotifyTokenRepository: SpotifyTokenRepository by lazy {
        SpotifyTokenLocalRepository(context)
    }

    override val albumsRepository: AlbumsRepository by lazy {
        AlbumsRemoteRepository(context)
    }

    override val tracksRepository: TracksRepository by lazy {
        TracksRemoteRepository(context)
    }
}