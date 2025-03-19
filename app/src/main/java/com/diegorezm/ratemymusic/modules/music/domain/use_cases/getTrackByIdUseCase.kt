package com.diegorezm.ratemymusic.modules.music.domain.use_cases

import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.TracksRepository
import com.diegorezm.ratemymusic.modules.music.domain.models.Track

suspend fun getTrackByIdUseCase(
    trackId: String,
    spotifyToken: String,
    trackRepository: TracksRepository
): Result<Track> {
    return trackRepository.getById(trackId, spotifyToken)
}