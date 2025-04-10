package com.diegorezm.ratemymusic.music.tracks.data.repositories

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackBulkDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackDTO
import com.diegorezm.ratemymusic.music.tracks.data.mappers.toDomain
import com.diegorezm.ratemymusic.music.tracks.data.network.RemoteTrackDataSource
import com.diegorezm.ratemymusic.music.tracks.domain.Track
import com.diegorezm.ratemymusic.music.tracks.domain.TracksRepository

class DefaultTrackRepository(
    private val remoteTrackDataSource: RemoteTrackDataSource
) : TracksRepository {
    override suspend fun getTrackById(id: String): Result<Track, DataError> {
        val result = remoteTrackDataSource.getTracktById(id)
        return when (result) {
            is Result.Error<DataError.Remote> -> Result.Error(result.error)
            is Result.Success<TrackDTO> -> Result.Success(result.data.toDomain())
        }
    }

    override suspend fun getTracksByIds(ids: List<String>): Result<List<Track>, DataError> {
        val result = remoteTrackDataSource.getTracksByIds(ids)
        return when (result) {
            is Result.Error<DataError.Remote> -> Result.Error(result.error)
            is Result.Success<TrackBulkDTO> -> Result.Success(result.data.tracks.map { it.toDomain() })
        }
    }

}