package com.diegorezm.ratemymusic.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.presentation.components.CarouselItem
import com.diegorezm.ratemymusic.core.presentation.components.HorizontalCarousel
import com.diegorezm.ratemymusic.core.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.core.presentation.toUiText
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    onAlbumClick: (String) -> Unit,
    onUnauthorized: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    HomeScreen(
        modifier = modifier.padding(16.dp),
        state = state.value,
        onAlbumClick = onAlbumClick,
        onUnauthorized = onUnauthorized
    )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    onUnauthorized: () -> Unit,
    onAlbumClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            is HomeState.Error -> {
                if (state.error == DataError.Remote.UNAUTHORIZED) {
                    onUnauthorized()
                } else {
                    Text(
                        text = state.error.toUiText().asString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            HomeState.Idle -> LoadingIndicator()
            HomeState.Loading -> LoadingIndicator()
            is HomeState.Success -> {
                val carouselItems = state.albums.map {
                    CarouselItem(
                        id = it.id,
                        name = it.name,
                        imageUrl = it.imageURL ?: "",
                        description = it.artists.joinToString(", ") { it.name }
                    )
                }
                HorizontalCarousel(items = carouselItems, onClick = onAlbumClick)
            }
        }

    }

}

@Preview(name = "HomeScreen")
@Composable
private fun PreviewHomeScreen() {
    val state = HomeState.Idle
    HomeScreen(state = state, onAlbumClick = {}, onUnauthorized = {})
}