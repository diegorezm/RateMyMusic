package com.diegorezm.ratemymusic.music.tracks.domain

import com.diegorezm.ratemymusic.core.domain.DataError

import com.diegorezm.ratemymusic.core.domain.Result

interface TracksRepository {
    suspend fun getTrackById(id: String): Result<Track, DataError>
    suspend fun getTracksByIds(ids: List<String>): Result<List<Track>, DataError>
}