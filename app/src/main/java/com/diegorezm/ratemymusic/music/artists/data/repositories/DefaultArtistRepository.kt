package com.diegorezm.ratemymusic.music.artists.data.repositories

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.music.albums.data.mappers.toDomain
import com.diegorezm.ratemymusic.music.albums.domain.Album
import com.diegorezm.ratemymusic.music.artists.data.mappers.toDomain
import com.diegorezm.ratemymusic.music.artists.data.network.RemoteArtistDataSource
import com.diegorezm.ratemymusic.music.artists.domain.Artist
import com.diegorezm.ratemymusic.music.artists.domain.ArtistRepository
import com.diegorezm.ratemymusic.music.shared.dto.PaginationDTO
import com.diegorezm.ratemymusic.music.tracks.data.mappers.toDomain
import com.diegorezm.ratemymusic.music.tracks.domain.Track

class DefaultArtistRepository(
    private val remoteArtistDataSource: RemoteArtistDataSource
) : ArtistRepository {
    override suspend fun getArtistById(id: String): Result<Artist, DataError> {
        val result = remoteArtistDataSource.getArtistById(id)
        return when (result) {
            is Result.Error -> Result.Error(result.error)
            is Result.Success -> Result.Success(result.data.toDomain())
        }
    }

    override suspend fun getArtistsByIds(ids: List<String>): Result<List<Artist>, DataError> {
        val result = remoteArtistDataSource.getArtistsByIds(ids)
        return when (result) {
            is Result.Error -> Result.Error(result.error)
            is Result.Success -> Result.Success(result.data.artists.map { it.toDomain() })
        }
    }

    override suspend fun getArtistTopTracks(id: String): Result<List<Track>, DataError> {
        val result = remoteArtistDataSource.getArtistTopTracks(id)
        return when (result) {
            is Result.Error -> Result.Error(result.error)
            is Result.Success -> Result.Success(result.data.tracks.map { it.toDomain() })
        }
    }

    override suspend fun getArtistAlbums(
        id: String,
        limit: Int,
        offset: Int
    ): Result<List<Album>, DataError> {
        val paginationDTO = PaginationDTO(limit, offset)
        val result = remoteArtistDataSource.getArtistAlbums(id, paginationDTO)
        return when (result) {
            is Result.Error -> Result.Error(result.error)
            is Result.Success -> Result.Success(result.data.albums.items.map { it.toDomain() })
        }
    }
}