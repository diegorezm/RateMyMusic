package com.diegorezm.ratemymusic.music.search.data.repositories

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.music.albums.data.mappers.toDomain
import com.diegorezm.ratemymusic.music.albums.domain.Album
import com.diegorezm.ratemymusic.music.artists.data.mappers.toDomain
import com.diegorezm.ratemymusic.music.artists.domain.Artist
import com.diegorezm.ratemymusic.music.search.data.network.RemoteSearchDataSource
import com.diegorezm.ratemymusic.music.search.domain.SearchRepository
import com.diegorezm.ratemymusic.music.shared.dto.PaginationDTO
import com.diegorezm.ratemymusic.music.tracks.data.mappers.toDomain
import com.diegorezm.ratemymusic.music.tracks.domain.Track

class DefaultSearchRepository(
    private val remoteSearchDataSource: RemoteSearchDataSource
) : SearchRepository {
    override suspend fun searchByAlbum(
        query: String,
        pagination: PaginationDTO
    ): Result<List<Album>, DataError> {
        val result = remoteSearchDataSource.searchByAlbum(query, pagination)
        return when (result) {
            is Result.Success -> {
                Result.Success(result.data.albums.items.map { it.toDomain() })
            }

            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }

    override suspend fun searchByTrack(
        query: String,
        pagination: PaginationDTO
    ): Result<List<Track>, DataError> {
        val result = remoteSearchDataSource.searchByTrack(query, pagination)
        return when (result) {
            is Result.Success -> {
                Result.Success(result.data.tracks.items.map { it.toDomain() })
            }

            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }

    override suspend fun searchByArtist(
        query: String,
        pagination: PaginationDTO
    ): Result<List<Artist>, DataError> {
        val result = remoteSearchDataSource.searchByArtist(query, pagination)
        return when (result) {
            is Result.Success -> {
                Result.Success(result.data.artists.items.map { it.toDomain() })
            }

            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }
}