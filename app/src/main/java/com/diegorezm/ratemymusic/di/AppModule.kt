package com.diegorezm.ratemymusic.di

import android.content.Context
import com.diegorezm.ratemymusic.BuildConfig
import com.diegorezm.ratemymusic.modules.favorites.data.repositories.FavoritesRepository
import com.diegorezm.ratemymusic.modules.favorites.domain.repositories.FavoritesRepositoryImpl
import com.diegorezm.ratemymusic.modules.followers.data.repositories.FollowersRepository
import com.diegorezm.ratemymusic.modules.followers.domain.repositories.FollowersRepositoryImpl
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.AlbumsRepository
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.SearchRepository
import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.TracksRepository
import com.diegorezm.ratemymusic.modules.music.domain.repositories.AlbumsRemoteRepository
import com.diegorezm.ratemymusic.modules.music.domain.repositories.SearchRemoteRepository
import com.diegorezm.ratemymusic.modules.music.domain.repositories.TracksRemoteRepository
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.domain.repositories.ProfileRepositoryImpl
import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository
import com.diegorezm.ratemymusic.modules.reviews.domain.repositories.ReviewsRepositoryImpl
import com.diegorezm.ratemymusic.modules.spotify_auth.data.local.repositories.SpotifyTokenRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.domain.repositories.SpotifyTokenLocalRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.ExternalAuthAction
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest

interface AppModule {
    val supabaseClient: SupabaseClient

    val db: Postgrest
    val auth: Auth

    val profileRepository: ProfileRepository
    val spotifyTokenRepository: SpotifyTokenRepository
    val albumsRepository: AlbumsRepository
    val tracksRepository: TracksRepository
    val reviewsRepository: ReviewsRepository
    val favoritesRepository: FavoritesRepository
    val searchRepository: SearchRepository
    val followersRepository: FollowersRepository
}

class AppModuleImpl(context: Context) : AppModule {

    @OptIn(SupabaseInternal::class)
    override val supabaseClient: SupabaseClient by lazy {
        val url = BuildConfig.SUPABASE_URL
        val apiKey = BuildConfig.SUPABASE_ANON_KEY

        val supabase = createSupabaseClient(
            supabaseUrl = url,
            supabaseKey = apiKey
        ) {
            install(Auth) {
                defaultExternalAuthAction =
                    ExternalAuthAction.CustomTabs()
            }
            install(Postgrest)

        }
        supabase
    }

    override val db: Postgrest by lazy {
        supabaseClient.postgrest
    }
    override val auth: Auth by lazy {
        supabaseClient.auth
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

    override val searchRepository: SearchRepository by lazy {
        SearchRemoteRepository(context)
    }

    override val followersRepository: FollowersRepository by lazy {
        FollowersRepositoryImpl(db)
    }
}