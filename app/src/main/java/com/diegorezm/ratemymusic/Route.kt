package com.diegorezm.ratemymusic

import kotlinx.serialization.Serializable

sealed class Route {
    @Serializable
    object AppGraph : Route()

    @Serializable
    object MainRoutes : Route()

    @Serializable
    object SignUp : Route()

    @Serializable
    object SignIn : Route()

    @Serializable
    object SpotifyAuth : Route()

    @Serializable
    data class AlbumDetails(val albumId: String) : Route()

    @Serializable
    data class ArtistDetails(val artistId: String) : Route()

    @Serializable
    data class TrackDetails(val trackId: String) : Route()

    @Serializable
    data class Profile(val userId: String) : Route()
}

sealed class MainRoute {
    @Serializable
    object MainRouteGraph : MainRoute()

    @Serializable
    object Home : MainRoute()

    @Serializable
    object Search : MainRoute()

    @Serializable
    object Profile : MainRoute()
}