package com.diegorezm.ratemymusic.di

import android.content.Context
import com.diegorezm.ratemymusic.modules.favorites.data.repositories.FavoritesRepository
import com.diegorezm.ratemymusic.modules.favorites.domain.repositories.FavoritesRepositoryImpl
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.AlbumsRepository
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.TracksRepository
import com.diegorezm.ratemymusic.modules.music.domain.repositories.AlbumsRemoteRepository
import com.diegorezm.ratemymusic.modules.music.domain.repositories.TracksRemoteRepository
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.domain.repositories.ProfileRepositoryImpl
import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository
import com.diegorezm.ratemymusic.modules.reviews.domain.repositories.ReviewsRepositoryImpl
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories.SpotifyTokenRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.domain.repositories.SpotifyTokenLocalRepository
import com.google.firebase.firestore.FirebaseFirestore

interface AppModule {
    val db: FirebaseFirestore

    val profileRepository: ProfileRepository
    val spotifyTokenRepository: SpotifyTokenRepository
    val albumsRepository: AlbumsRepository
    val tracksRepository: TracksRepository
    val reviewsRepository: ReviewsRepository
    val favoritesRepository: FavoritesRepository
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

    override val reviewsRepository: ReviewsRepository by lazy {
        ReviewsRepositoryImpl(db)
    }

    override val favoritesRepository: FavoritesRepository by lazy {
        FavoritesRepositoryImpl(db)
    }
}