package com.diegorezm.ratemymusic.modules.music.data.remote.api

import com.diegorezm.ratemymusic.modules.music.data.remote.models.AlbumDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.TracksDTO
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AlbumsApi {
    @GET("{id}")
    suspend fun getAlbumById(
        @Path("id") albumId: String,
        @Header("Authorization") authToken: String
    ): AlbumDTO

    @GET("{id}/tracks")
    suspend fun getAlbumTracks(
        @Path("id") albumId: String,
        @Header("Authorization") authToken: String
    ): List<TracksDTO>

    @GET("browse/new-releases")
    suspend fun getNewReleases(
        @Header("Authorization") authToken: String
    ): List<AlbumDTO>
}