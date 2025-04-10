package com.diegorezm.ratemymusic.music.albums.data.repositories

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.modules.music.data.remote.models.AlbumBulkDTO
import com.diegorezm.ratemymusic.music.albums.data.dto.AlbumDTO
import com.diegorezm.ratemymusic.music.albums.data.dto.PaginatedAlbumDTO
import com.diegorezm.ratemymusic.music.albums.data.mappers.toDomain
import com.diegorezm.ratemymusic.music.albums.data.network.RemoteAlbumDataSource
import com.diegorezm.ratemymusic.music.albums.domain.Album
import com.diegorezm.ratemymusic.music.albums.domain.AlbumsRepository

class DefaultAlbumRepository(
    private val remoteAlbumRepository: RemoteAlbumDataSource
) : AlbumsRepository {
    override suspend fun getAlbumById(id: String): Result<Album, DataError.Remote> {
        val res = remoteAlbumRepository.getAlbumById(id)
        return when (res) {
            is Result.Error<DataError.Remote> -> Result.Error(res.error)
            is Result.Success<AlbumDTO> -> Result.Success(res.data.toDomain())
        }
    }

    override suspend fun getAlbumsByIds(ids: List<String>): Result<List<Album>, DataError.Remote> {
        val res = remoteAlbumRepository.getAlbumsByIds(ids)
        return when (res) {
            is Result.Error<DataError.Remote> -> Result.Error(res.error)
            is Result.Success<AlbumBulkDTO> -> Result.Success(res.data.albums.map { it.toDomain() })
        }

    }

    override suspend fun getNewReleases(): Result<List<Album>, DataError.Remote> {
        val res = remoteAlbumRepository.getNewReleases()
        return when (res) {
            is Result.Error<DataError.Remote> -> Result.Error(res.error)
            is Result.Success<PaginatedAlbumDTO> -> Result.Success(res.data.albums.items.map { it.toDomain() })
        }
    }
}