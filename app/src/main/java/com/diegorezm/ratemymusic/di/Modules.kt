package com.diegorezm.ratemymusic.di

import android.content.Context
import androidx.room.Room
import com.diegorezm.ratemymusic.BuildConfig
import com.diegorezm.ratemymusic.auth.data.repositories.DefaultAuthRepository
import com.diegorezm.ratemymusic.auth.domain.AuthRepository
import com.diegorezm.ratemymusic.auth.presentation.AuthViewModel
import com.diegorezm.ratemymusic.auth.presentation.sign_in.SignInViewModel
import com.diegorezm.ratemymusic.auth.presentation.sign_up.SignUpViewModel
import com.diegorezm.ratemymusic.core.data.HttpClientFactory
import com.diegorezm.ratemymusic.profile.data.repositories.DefaultProfileRepository
import com.diegorezm.ratemymusic.profile.domain.repositories.ProfileRepository
import com.diegorezm.ratemymusic.spotify_auth.data.database.SpotifyTokenDatabase
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.ExternalAuthAction
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(get()) }
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

    viewModelOf(::AuthViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::SignInViewModel)
}

val spotifyDatabaseModule = module {
    single {
        Room.databaseBuilder(
            get<Context>(), // Get the application context
            SpotifyTokenDatabase::class.java,
            SpotifyTokenDatabase.DATABASE_NAME
        ).build()
    }
    single { get<SpotifyTokenDatabase>().spotifyTokenDao }
}
