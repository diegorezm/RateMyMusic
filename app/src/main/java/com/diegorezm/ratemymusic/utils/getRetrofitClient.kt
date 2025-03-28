package com.diegorezm.ratemymusic.utils

import android.content.Context
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object RetrofitClient {
    private const val CACHE_SIZE = 10L * 1024 * 1024
    private const val MAX_CACHE_AGE = 1

    private fun offlineCacheInterceptor(context: Context) = Interceptor { chain ->
        var request = chain.request()
        if (!isNetworkAvailable(context)) {
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=86400")
                .build()
        }
        chain.proceed(request)
    }

    private fun cacheInterceptor() = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        response.newBuilder()
            .header("Cache-Control", "public, max-age=${MAX_CACHE_AGE * 24 * 60 * 60}")
            .build()
    }

    fun getRetrofit(context: Context, baseUrl: String): Retrofit {
        val cache = Cache(File(context.cacheDir, "http_cache"), CACHE_SIZE)

        val client = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(offlineCacheInterceptor(context))
            .addNetworkInterceptor(cacheInterceptor())
            .addInterceptor {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                logging.intercept(it)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        return connectivityManager.activeNetwork != null
    }
}
