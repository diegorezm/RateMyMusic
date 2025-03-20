package com.diegorezm.ratemymusic.modules.music.data.remote.api

import com.diegorezm.ratemymusic.modules.music.data.remote.models.SearchDTO
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchApi {
    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Header("Authorization") authToken: String
    ): SearchDTO
}

enum class SearchType(val type: String) {
    ALBUM("album"),
    TRACK("track"),
    ARTIST("artist")
}