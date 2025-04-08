package com.diegorezm.ratemymusic.music.albums.domain

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result

interface AlbumsRepository {
    suspend fun getAlbumById(id: String): Result<Album, DataError.Remote>
    suspend fun getAlbumsByIds(ids: List<String>): Result<List<Album>, DataError.Remote>
    suspend fun getNewReleases(): Result<List<Album>, DataError.Remote>
}