package com.diegorezm.ratemymusic

sealed class Route {
    

    data class AlbumDetails(val albumId: String) : Route()
    data class ArtistDetails(val artistId: String) : Route()
    data class TrackDetails(val trackId: String) : Route()
}
