package com.diegorezm.ratemymusic.music.tracks.data.network

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackBulkDTO
import com.diegorezm.ratemymusic.modules.music.data.remote.models.TrackDTO

interface RemoteTrackDataSource {
    suspend fun getTracktById(id: String): Result<TrackDTO, DataError.Remote>
    suspend fun getTracksByIds(ids: List<String>): Result<TrackBulkDTO, DataError.Remote>
}