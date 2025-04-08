package com.diegorezm.ratemymusic.music.albums.data.repositories

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.music.albums.data.dto.AlbumDTO
import com.diegorezm.ratemymusic.music.albums.data.dto.PaginatedAlbumDTO
import com.diegorezm.ratemymusic.music.albums.data.mappers.toDomain
import com.diegorezm.ratemymusic.music.albums.data.network.RemoteAlbumRepository
import com.diegorezm.ratemymusic.music.albums.domain.Album
import com.diegorezm.ratemymusic.music.albums.domain.AlbumsRepository

class DefaultAlbumRepository(
    private val remoteAlbumRepository: RemoteAlbumRepository
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
            is Result.Success<List<AlbumDTO>> -> Result.Success(res.data.map { it.toDomain() })
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