package com.diegorezm.ratemymusic.modules.music.data.remote.repositories

import com.diegorezm.ratemymusic.modules.music.domain.models.Track

interface TracksRepository {
    suspend fun getById(id: String, spotifyAuthToken: String?): Result<Track>
    suspend fun getByIds(ids: List<String>, spotifyAuthToken: String?): Result<List<Track>>
}