package com.diegorezm.ratemymusic.music.albums.data.network

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.modules.music.data.remote.models.AlbumBulkDTO
import com.diegorezm.ratemymusic.music.albums.data.dto.AlbumDTO
import com.diegorezm.ratemymusic.music.albums.data.dto.PaginatedAlbumDTO

interface RemoteAlbumDataSource {
    suspend fun getAlbumById(id: String): Result<AlbumDTO, DataError.Remote>
    suspend fun getAlbumsByIds(ids: List<String>): Result<AlbumBulkDTO, DataError.Remote>
    suspend fun getNewReleases(): Result<PaginatedAlbumDTO, DataError.Remote>
}