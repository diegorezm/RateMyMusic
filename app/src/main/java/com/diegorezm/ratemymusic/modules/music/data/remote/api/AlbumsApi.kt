package com.diegorezm.ratemymusic.modules.music.data.remote.api

import com.diegorezm.ratemymusic.modules.music.data.remote.models.AlbumBulkDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.AlbumDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.PaginatedDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackSimpleDTO
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface AlbumsApi {
    @GET("albums/{id}")
    suspend fun getAlbumById(
        @Path("id") albumId: String,
        @Header("Authorization") authToken: String
    ): AlbumDTO

    @GET("albums/{id}/tracks")
    suspend fun getAlbumTracks(
        @Path("id") albumId: String,
        @Header("Authorization") authToken: String
    ): PaginatedDTO<TrackSimpleDTO>

    @GET("albums")
    suspend fun getSeveralAlbums(
        @Query("ids") albumIds: String,
        @Header("Authorization") authToken: String
    ): AlbumBulkDTO

    @GET("albums/browse/new-releases")
    suspend fun getNewReleases(
        @Header("Authorization") authToken: String
    ): List<AlbumDTO>


}