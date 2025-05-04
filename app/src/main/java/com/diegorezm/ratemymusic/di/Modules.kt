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
import com.diegorezm.ratemymusic.followers.data.repositories.DefaultFollowersRepository
import com.diegorezm.ratemymusic.followers.domain.FollowersRepository
import com.diegorezm.ratemymusic.home.presentation.HomeViewModel
import com.diegorezm.ratemymusic.music.albums.data.network.KtorRemoteAlbumDataSource
import com.diegorezm.ratemymusic.music.albums.data.network.RemoteAlbumDataSource
import com.diegorezm.ratemymusic.music.albums.data.repositories.DefaultAlbumRepository
import com.diegorezm.ratemymusic.music.albums.domain.AlbumsRepository
import com.diegorezm.ratemymusic.music.albums.presentation.AlbumViewModel
import com.diegorezm.ratemymusic.music.artists.data.network.KtorRemoteArtistDataSource
import com.diegorezm.ratemymusic.music.artists.data.network.RemoteArtistDataSource
import com.diegorezm.ratemymusic.music.artists.data.repositories.DefaultArtistRepository
import com.diegorezm.ratemymusic.music.artists.domain.ArtistRepository
import com.diegorezm.ratemymusic.music.artists.presentation.ArtistViewModel
import com.diegorezm.ratemymusic.music.search.data.network.KtorRemoteSearchDataSource
import com.diegorezm.ratemymusic.music.search.data.network.RemoteSearchDataSource
import com.diegorezm.ratemymusic.music.search.data.repositories.DefaultSearchRepository
import com.diegorezm.ratemymusic.music.search.domain.SearchRepository
import com.diegorezm.ratemymusic.music.search.presentation.SearchViewModel
import com.diegorezm.ratemymusic.music.tracks.data.network.KtorRemoteTrackDataSource
import com.diegorezm.ratemymusic.music.tracks.data.network.RemoteTrackDataSource
import com.diegorezm.ratemymusic.music.tracks.data.repositories.DefaultTrackRepository
import com.diegorezm.ratemymusic.music.tracks.domain.TracksRepository
import com.diegorezm.ratemymusic.music.tracks.presentation.TrackScreenViewModel
import com.diegorezm.ratemymusic.profile.data.repositories.DefaultProfileRepository
import com.diegorezm.ratemymusic.profile.domain.repositories.ProfileRepository
import com.diegorezm.ratemymusic.profile.presentation.ProfileViewModel
import com.diegorezm.ratemymusic.reviews.data.repositories.DefaultReviewRepository
import com.diegorezm.ratemymusic.reviews.domain.ReviewRepository
import com.diegorezm.ratemymusic.reviews.presentation.ReviewViewModel
import com.diegorezm.ratemymusic.spotify_auth.data.database.SpotifyTokenDatabase
import com.diegorezm.ratemymusic.spotify_auth.data.network.KtorRemoteSpotifyAuthDataSource
import com.diegorezm.ratemymusic.spotify_auth.data.network.RemoteSpotifyAuthDataSource
import com.diegorezm.ratemymusic.spotify_auth.data.repositories.DefaultSpotifyTokenRepository
import com.diegorezm.ratemymusic.spotify_auth.domain.SpotifyTokenRepository
import com.diegorezm.ratemymusic.spotify_auth.presentation.SpotifyAuthViewModel
import com.diegorezm.ratemymusic.user_favorites.data.repositories.DefaultUserFavoritesRepository
import com.diegorezm.ratemymusic.user_favorites.domain.UserFavoritesRepository
import com.diegorezm.ratemymusic.user_favorites.presentation.UserFavoritesViewModel
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
import org.koin.core.module.dsl.viewModel
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

    singleOf(::KtorRemoteArtistDataSource).bind<RemoteArtistDataSource>()
    singleOf(::DefaultArtistRepository).bind<ArtistRepository>()

    singleOf(::KtorRemoteSearchDataSource).bind<RemoteSearchDataSource>()
    singleOf(::DefaultSearchRepository).bind<SearchRepository>()

    singleOf(::DefaultReviewRepository).bind<ReviewRepository>()

    singleOf(::DefaultUserFavoritesRepository).bind<UserFavoritesRepository>()

    singleOf(::DefaultFollowersRepository).bind<FollowersRepository>()

    viewModelOf(::ArtistViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::TrackScreenViewModel)
    viewModelOf(::AlbumViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::SpotifyAuthViewModel)

    viewModel { params ->
        ReviewViewModel(
            repository = get(),
            auth = get(),
            filter = params.get()
        )
    }

    viewModel { params ->
        ProfileViewModel(
            profileRepository = get(),
            followersRepository = get(),
            authRepository = get(),
            profileId = params.get(),
            currentUserId = params.get(),
            onSignOut = params.get()
        )
    }
    viewModel { params ->
        UserFavoritesViewModel(
            userFavoritesRepository = get(),
            albumRepository = get(),
            trackRepository = get(),
            artistRepository = get(),
            profileId = params.get()
        )
    }

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
