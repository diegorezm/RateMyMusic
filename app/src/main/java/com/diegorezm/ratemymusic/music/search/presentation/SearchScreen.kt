package com.diegorezm.ratemymusic.music.search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.app.Route
import com.diegorezm.ratemymusic.core.presentation.theme.RateMyMusicTheme
import com.diegorezm.ratemymusic.music.search.presentation.components.SearchBar
import com.diegorezm.ratemymusic.music.search.presentation.components.SearchFilters
import com.diegorezm.ratemymusic.music.search.presentation.components.SearchResults
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    navController: NavController
) {
    val searchState by viewModel.state.collectAsState()
    SearchScreen(modifier = modifier.padding(16.dp), state = searchState, onAction = {
        when (it) {
            is SearchScreenActions.OnAlbumClick -> {
                val route = Route.AlbumDetails(it.albumId)
                navController.navigate(route)
            }

            is SearchScreenActions.OnTrackClick -> {
                val route = Route.TrackDetails(it.trackId)
                navController.navigate(route)
            }

            is SearchScreenActions.OnArtistClick -> Unit
            else -> viewModel.onAction(it)
        }
    })
}

@Composable
private fun SearchScreen(
    modifier: Modifier = Modifier,
    state: SearchState,
    onAction: (SearchScreenActions) -> Unit
) {
    Column(modifier) {
        SearchBar(query = state.query, onQueryChanged = {
            onAction(SearchScreenActions.OnQueryChange(it))
        })
        SearchFilters(
            currentFilter = state.searchingBy,
            onFilterChange = {
                onAction(SearchScreenActions.OnFilterChange(it))
            }
        )
        SearchResults(state = state, onAction = onAction)
    }
}

@Preview(name = "SearchScreen")
@Composable
private fun PreviewSearchScreen() {
    RateMyMusicTheme {
        SearchScreen(state = SearchState(), onAction = {})
    }

}