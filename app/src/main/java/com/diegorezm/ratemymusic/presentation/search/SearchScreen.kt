package com.diegorezm.ratemymusic.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.diegorezm.ratemymusic.AlbumRouteId
import com.diegorezm.ratemymusic.TrackRouteId
import com.diegorezm.ratemymusic.modules.music.data.remote.api.RemoteSearchType
import com.diegorezm.ratemymusic.modules.music.domain.models.AlbumSimple
import com.diegorezm.ratemymusic.modules.music.domain.models.Artist
import com.diegorezm.ratemymusic.modules.music.domain.models.Track
import com.diegorezm.ratemymusic.modules.music.domain.repositories.SearchMockRepository
import com.diegorezm.ratemymusic.modules.spotify_auth.domain.repositories.SpotifyTokenMockRepository
import com.diegorezm.ratemymusic.presentation.components.ErrorMessage
import com.diegorezm.ratemymusic.ui.theme.RateMyMusicTheme


@Composable
fun SearchScreen(viewModel: SearchViewModel, navController: NavController) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchState by viewModel.searchState.collectAsState()
    val searchingFor by viewModel.searchingFor.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChanged = viewModel::setSearchQuery
        )

        Spacer(modifier = Modifier.height(8.dp))

        SearchTypeSelector(
            selectedType = searchingFor,
            onTypeSelected = viewModel::setSearchingFor
        )
        Spacer(modifier = Modifier.height(16.dp))
        SearchResults(searchState, navController)
    }
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search for music...", color = MaterialTheme.colorScheme.onSurface) },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        singleLine = true,
        shape = MaterialTheme.shapes.medium,
    )
}

@Composable
fun SearchTypeSelector(selectedType: RemoteSearchType, onTypeSelected: (RemoteSearchType) -> Unit) {
    val selectedIndex = RemoteSearchType.entries.indexOf(selectedType)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RemoteSearchType.entries.forEachIndexed { index, type ->
            val isSelected = index == selectedIndex
            FilterChip(
                selected = isSelected,
                label = {
                    Text(type.name.lowercase())
                },
                onClick = {
                    onTypeSelected(type)
                },
                modifier = Modifier.padding(end = 8.dp),
                enabled = true,
                shape = MaterialTheme.shapes.medium,
            )
        }
    }
}

@Composable
fun SearchResults(searchState: SearchState, navController: NavController) {
    when (searchState) {
        is SearchState.Idle -> Text("Enter a search query")
        is SearchState.Loading -> CircularProgressIndicator()
        is SearchState.Error -> {
            val message = searchState.message
            ErrorMessage(message)
        }

        is SearchState.Success<*> -> {
            val res = searchState.result

            if (res.items.isEmpty()) {
                Text("No results found.")
                return
            }

            val items = res.items

            // Idk about you but i don't know any List<T> that has a bunch of different T's (this
            // unchecked cast pissed me off)
            when (items.firstOrNull()) {
                is AlbumSimple -> {
                    @Suppress("UNCHECKED_CAST")
                    val albums = items as List<AlbumSimple>
                    val items = albums.map {
                        SearchItem(
                            id = it.id,
                            title = it.name,
                            subtitle = it.artists.joinToString(", ") { it.name },
                            imageURL = it.imageURL ?: ""
                        )
                    }
                    SearchList(items, onNavClick = {
                        val routeId = AlbumRouteId(it)
                        navController.navigate(routeId)
                    })
                }

                is Artist -> {
                    @Suppress("UNCHECKED_CAST")
                    val artists = items as List<Artist>
                    val items = artists.map {
                        SearchItem(
                            id = it.id,
                            title = it.name,
                            subtitle = it.genres.joinToString(", "),
                            imageURL = it.imageURL
                        )
                    }
                    SearchList(items, onNavClick = {
                    })
                }

                is Track -> {
                    @Suppress("UNCHECKED_CAST")
                    val tracks = items as List<Track>
                    val items = tracks.map {
                        SearchItem(
                            id = it.id,
                            title = it.name,
                            subtitle = it.artists.joinToString(", ") { it.name },
                            imageURL = it.albumCoverURL
                        )
                    }
                    SearchList(items, onNavClick = {
                        val routeId = TrackRouteId(it)
                        navController.navigate(routeId)
                    })
                }

                else -> {
                    Text("No results found.")
                }
            }

        }
    }
}


@Composable
@Preview(showBackground = true)
fun SearchScreenPreview() {
    val viewModel = SearchViewModel(SearchMockRepository(), SpotifyTokenMockRepository())
    val navController = rememberNavController()
    RateMyMusicTheme {
        SearchScreen(viewModel, navController)
    }
}
