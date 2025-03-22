package com.diegorezm.ratemymusic.modules.music.domain.use_cases

import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.TracksRepository
import com.diegorezm.ratemymusic.modules.music.domain.models.Track

suspend fun getTrackByIds(
    ids: List<String>,
    spotifyAuthToken: String?,
    tracksRepository: TracksRepository
): Result<List<Track>> {
    return tracksRepository.getByIds(ids, spotifyAuthToken)
}