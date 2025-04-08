package com.diegorezm.ratemymusic.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.diegorezm.ratemymusic.BuildConfig
import com.diegorezm.ratemymusic.auth.data.repositories.DefaultAuthRepository
import com.diegorezm.ratemymusic.auth.domain.AuthRepository
import com.diegorezm.ratemymusic.auth.presentation.AuthViewModel
import com.diegorezm.ratemymusic.auth.presentation.sign_in.SignInViewModel
import com.diegorezm.ratemymusic.auth.presentation.sign_up.SignUpViewModel
import com.diegorezm.ratemymusic.core.data.HttpClientFactory
import com.diegorezm.ratemymusic.home.presentation.HomeViewModel
import com.diegorezm.ratemymusic.music.albums.data.network.KtorRemoteAlbumDataSource
import com.diegorezm.ratemymusic.music.albums.data.network.RemoteAlbumDataSource
import com.diegorezm.ratemymusic.music.albums.data.repositories.DefaultAlbumRepository
import com.diegorezm.ratemymusic.music.albums.domain.AlbumsRepository
import com.diegorezm.ratemymusic.music.albums.presentation.AlbumViewModel
import com.diegorezm.ratemymusic.music.tracks.data.network.KtorRemoteTrackDataSource
import com.diegorezm.ratemymusic.music.tracks.data.network.RemoteTrackDataSource
import com.diegorezm.ratemymusic.music.tracks.data.repositories.DefaultTrackRepository
import com.diegorezm.ratemymusic.music.tracks.domain.TracksRepository
import com.diegorezm.ratemymusic.music.tracks.presentation.TrackScreenViewModel
import com.diegorezm.ratemymusic.profile.data.repositories.DefaultProfileRepository
import com.diegorezm.ratemymusic.profile.domain.repositories.ProfileRepository
import com.diegorezm.ratemymusic.spotify_auth.data.database.SpotifyTokenDatabase
import com.diegorezm.ratemymusic.spotify_auth.data.network.KtorRemoteSpotifyAuthDataSource
import com.diegorezm.ratemymusic.spotify_auth.data.network.RemoteSpotifyAuthDataSource
import com.diegorezm.ratemymusic.spotify_auth.data.repositories.DefaultSpotifyTokenRepository
import com.diegorezm.ratemymusic.spotify_auth.domain.SpotifyTokenRepository
import com.diegorezm.ratemymusic.spotify_auth.presentation.SpotifyAuthViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.ExternalAuthAction
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(get()) }
    single<HttpClientEngine> { OkHttp.create() }
    single {
        val url = BuildConfig.SUPABASE_URL
        val apiKey = BuildConfig.SUPABASE_ANON_KEY

        createSupabaseClient(
            supabaseUrl = url,
            supabaseKey = apiKey
        ) {
            install(Auth) {
                defaultExternalAuthAction =
                    ExternalAuthAction.CustomTabs()
            }
            install(Postgrest)
        }
    }

    single { get<SupabaseClient>().postgrest }
    single { get<SupabaseClient>().auth }

    singleOf(::DefaultProfileRepository).bind<ProfileRepository>()
    singleOf(::DefaultAuthRepository).bind<AuthRepository>()

    singleOf(::KtorRemoteSpotifyAuthDataSource).bind<RemoteSpotifyAuthDataSource>()
    singleOf(::DefaultSpotifyTokenRepository).bind<SpotifyTokenRepository>()

    singleOf(::KtorRemoteAlbumDataSource).bind<RemoteAlbumDataSource>()
    singleOf(::DefaultAlbumRepository).bind<AlbumsRepository>()

    singleOf(::DefaultTrackRepository).bind<TracksRepository>()
    singleOf(::KtorRemoteTrackDataSource).bind<RemoteTrackDataSource>()

    viewModelOf(::TrackScreenViewModel)
    viewModelOf(::AlbumViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::SpotifyAuthViewModel)
}

val spotifyDatabaseModule = module {
    single {
        Room.databaseBuilder(
            get<Context>(),
            SpotifyTokenDatabase::class.java,
            SpotifyTokenDatabase.DATABASE_NAME
        ).setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<SpotifyTokenDatabase>().spotifyTokenDao }
}
