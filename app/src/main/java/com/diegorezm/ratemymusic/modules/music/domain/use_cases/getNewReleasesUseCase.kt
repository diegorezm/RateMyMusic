package com.diegorezm.ratemymusic.modules.music.domain.use_cases

import com.diegorezm.ratemymusic.modules.music.data.remote.repositories.AlbumsRepository
import com.diegorezm.ratemymusic.modules.music.domain.models.AlbumSimple

suspend fun getNewReleasesUseCase(
    limit: Int = 10,
    offset: Int = 0,
    spotifyAuthToken: String? = null,
    repository: AlbumsRepository
): Result<List<AlbumSimple>> {
    return repository.getNewReleases(limit, offset, spotifyAuthToken)
}