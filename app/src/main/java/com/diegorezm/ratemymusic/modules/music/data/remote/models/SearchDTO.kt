package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.google.gson.annotations.SerializedName

data class SearchDTO(
    @SerializedName("albums") val albums: PaginatedDTO<AlbumSimpleDTO>?,
    @SerializedName("artists") val artists: PaginatedDTO<ArtistDTO>?,
    @SerializedName("tracks") val tracks: PaginatedDTO<TrackDTO>?
)
