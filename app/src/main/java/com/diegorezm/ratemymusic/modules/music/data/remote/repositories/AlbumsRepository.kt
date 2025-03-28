package com.diegorezm.ratemymusic.modules.music.data.remote.repositories

import com.diegorezm.ratemymusic.modules.music.domain.models.Album
import com.diegorezm.ratemymusic.modules.music.domain.models.AlbumSimple
import com.diegorezm.ratemymusic.modules.music.domain.models.PaginatedResult
import com.diegorezm.ratemymusic.modules.music.domain.models.TrackSimple

interface AlbumsRepository {
    suspend fun getById(albumId: String, spotifyAuthToken: String? = null): Result<Album>
    suspend fun getByIds(
        albumIds: List<String>,
        spotifyAuthToken: String? = null
    ): Result<List<Album>>

    suspend fun getTracks(
        albumId: String,
        spotifyAuthToken: String? = null
    ): Result<PaginatedResult<TrackSimple>>

    suspend fun getNewReleases(
        limit: Int,
        offset: Int,
        spotifyAuthToken: String? = null
    ): Result<List<AlbumSimple>>
}