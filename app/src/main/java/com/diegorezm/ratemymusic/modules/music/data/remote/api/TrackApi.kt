package com.diegorezm.ratemymusic.modules.music.data.remote.api

import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackBulkDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackDTO
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TracksApi {
    @GET("tracks/{id}")
    suspend fun getTrackById(
        @Path("id") trackId: String,
        @Header("Authorization") authToken: String
    ): TrackDTO

    @GET("tracks")
    suspend fun getTrackByIds(
        @Query("ids") trackIds: List<String>,
        @Header("Authorization") authToken: String
    ): TrackBulkDTO

}