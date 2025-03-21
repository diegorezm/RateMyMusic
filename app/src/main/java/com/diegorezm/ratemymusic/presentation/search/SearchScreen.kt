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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.AlbumRouteId
import com.diegorezm.ratemymusic.TrackRouteId
import com.diegorezm.ratemymusic.modules.music.data.remote.api.SearchType
import com.diegorezm.ratemymusic.modules.music.domain.models.AlbumSimple
import com.diegorezm.ratemymusic.modules.music.domain.models.Artist
import com.diegorezm.ratemymusic.modules.music.domain.models.Track


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
        placeholder = { Text("Search for music...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        singleLine = true,
        shape = MaterialTheme.shapes.medium,
    )
}

@Composable
fun SearchTypeSelector(selectedType: SearchType, onTypeSelected: (SearchType) -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        SearchType.entries.forEach { type ->
            Button(
                onClick = { onTypeSelected(type) },
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedType == type) MaterialTheme.colorScheme.primary else Color.Gray
                )
            ) {
                Text(type.name)
            }
        }
    }
}

@Composable
fun SearchResults(searchState: SearchState, navController: NavController) {
    when (searchState) {
        is SearchState.Idle -> Text("Enter a search query")
        is SearchState.Loading -> CircularProgressIndicator()
        is SearchState.Error -> {
            Text("Error: ${searchState.message}", color = Color.Red)
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
                    AlbumList(albums, onNavClick = {
                        val routeId = AlbumRouteId(it)
                        navController.navigate(routeId)
                    })
                }

                is Artist -> {
                    @Suppress("UNCHECKED_CAST")
                    val artists = items as List<Artist>
                    ArtistList(artists, onNavClick = {})
                }

                is Track -> {
                    @Suppress("UNCHECKED_CAST")
                    val tracks = items as List<Track>
                    TrackList(tracks, onNavClick = {
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
