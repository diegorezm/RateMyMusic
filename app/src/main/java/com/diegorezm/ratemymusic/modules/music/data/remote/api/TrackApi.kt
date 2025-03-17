package com.diegorezm.ratemymusic.modules.music.data.remote.api

import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackDTO
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface TracksApi {
    @GET("{id}")
    suspend fun getTrackById(
        @Path("id") trackId: String,
        @Header("Authorization") authToken: String
    ): TrackDTO
}