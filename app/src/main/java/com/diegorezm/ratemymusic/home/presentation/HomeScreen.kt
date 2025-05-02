package com.diegorezm.ratemymusic.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.presentation.components.CarouselItem
import com.diegorezm.ratemymusic.core.presentation.components.HorizontalCarousel
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
        onUnauthorized = onUnauthorized,
        onRefresh = {
            viewModel.fetchAlbums(true)
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    onRefresh: () -> Unit,
    onUnauthorized: () -> Unit,
    onAlbumClick: (String) -> Unit
) {
    val pullRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = state is HomeState.Loading,
        onRefresh = onRefresh,
        state = pullRefreshState,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = state is HomeState.Loading,
                containerColor = MaterialTheme.colorScheme.primary,
                color = MaterialTheme.colorScheme.onPrimary,
                state = pullRefreshState
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                is HomeState.Error -> {
                    item {
                        if (state.error == DataError.Remote.UNAUTHORIZED) {
                            onUnauthorized()
                        } else {
                            Text(
                                text = state.error.toUiText().asString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                }

                is HomeState.Success -> {
                    val carouselItems = state.albums.map {
                        CarouselItem(
                            id = it.id,
                            name = it.name,
                            imageUrl = it.imageURL ?: "",
                            description = it.artists.joinToString(", ") { it.name }
                        )
                    }
                    item {
                        Column {
                            Text(
                                text = stringResource(R.string.latest_releases),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalCarousel(items = carouselItems, onClick = onAlbumClick)
                        }
                    }
                }

                else -> {
                }
            }

        }
    }

}

@Preview(name = "HomeScreen")
@Composable
private fun PreviewHomeScreen() {
    val state = HomeState.Idle
    HomeScreen(state = state, onAlbumClick = {}, onUnauthorized = {}, onRefresh = {})
}