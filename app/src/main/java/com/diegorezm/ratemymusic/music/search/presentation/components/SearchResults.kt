package com.diegorezm.ratemymusic.music.search.presentation.components

import androidx.compose.runtime.Composable
import com.diegorezm.ratemymusic.core.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.music.albums.domain.Album
import com.diegorezm.ratemymusic.music.artists.domain.Artist
import com.diegorezm.ratemymusic.music.search.presentation.SearchScreenActions
import com.diegorezm.ratemymusic.music.search.presentation.SearchState
import com.diegorezm.ratemymusic.music.search.presentation.SearchType
import com.diegorezm.ratemymusic.music.tracks.domain.Track

@Composable
fun SearchResults(state: SearchState, onAction: (SearchScreenActions) -> Unit) {
    if (state.isLoading) LoadingIndicator()
    when (state.searchingBy) {
        SearchType.TRACK -> TrackSearchResult(tracks = state.tracks, onClick = {
            onAction(SearchScreenActions.OnTrackClick(it))
        })

        SearchType.ALBUM -> AlbumSearchResult(albums = state.albums, onClick = {
            onAction(SearchScreenActions.OnAlbumClick(it))
        })

        SearchType.ARTIST -> ArtistSearchResult(artists = state.artists, onClick = {
            onAction(SearchScreenActions.OnArtistClick(it))
        })
    }
}

@Composable
private fun AlbumSearchResult(albums: List<Album>, onClick: (String) -> Unit) {
    val items = albums.map {
        SearchItem(
            id = it.id,
            title = it.name,
            subtitle = it.artists.joinToString(", ") { it.name },
            imageURL = it.imageURL ?: ""
        )
    }
    SearchList(items = items, onClick = {
        onClick(it)
    })

}

@Composable
private fun TrackSearchResult(tracks: List<Track>, onClick: (String) -> Unit) {
    val items = tracks.map {
        SearchItem(
            id = it.id,
            title = it.name,
            subtitle = it.artists.joinToString(", ") { it.name },
            imageURL = it.albumCoverURL ?: ""
        )
    }

    SearchList(items = items, onClick = {
        onClick(it)
    })

}

@Composable
private fun ArtistSearchResult(artists: List<Artist>, onClick: (String) -> Unit) {
    val items = artists.map {
        SearchItem(
            id = it.id,
            title = it.name,
            subtitle = it.genres.joinToString(", "),
            imageURL = it.imageURL ?: ""
        )
    }

    SearchList(items = items, onClick = {
        onClick(it)
    })

}