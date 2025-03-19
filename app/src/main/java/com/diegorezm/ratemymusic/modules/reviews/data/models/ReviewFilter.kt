package com.diegorezm.ratemymusic.modules.reviews.data.models

sealed class ReviewFilter {
    data class ByAlbum(val albumId: String) : ReviewFilter()
    data class ByTrack(val trackId: String) : ReviewFilter()
    data class ByUser(val userId: String) : ReviewFilter()
}