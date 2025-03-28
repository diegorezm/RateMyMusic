package com.diegorezm.ratemymusic.modules.music.data.remote.models

import com.google.gson.annotations.SerializedName

data class NewReleaseDTO(
    @SerializedName("albums") val albums: PaginatedDTO<AlbumSimpleDTO>
)
