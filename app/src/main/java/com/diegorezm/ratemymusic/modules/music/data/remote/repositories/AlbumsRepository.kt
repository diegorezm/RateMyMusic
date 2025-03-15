package com.diegorezm.ratemymusic.modules.music.data.remote.repositories

import com.diegorezm.ratemymusic.modules.music.domain.models.Album

interface AlbumsRepository {
    suspend fun getById(albumId: String, spotifyAuthToken: String? = null): Result<Album>
    suspend fun getTracks(albumId: String, spotifyAuthToken: String? = null): Result<List<Album>>
    suspend fun getNewReleases(spotifyAuthToken: String? = null): Result<List<Album>>
}