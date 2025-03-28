package com.diegorezm.ratemymusic.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.AlbumRouteId
import com.diegorezm.ratemymusic.presentation.components.CarouselItem
import com.diegorezm.ratemymusic.presentation.components.HorizontalCarousel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    navController: NavController
) {
    val newReleases by homeViewModel.newReleases.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val carouselItems = newReleases.map {
            CarouselItem(
                id = it.id,
                name = it.name,
                imageUrl = it.imageURL ?: "",
                description = it.artists.joinToString(", ") { it.name }
            )
        }
        Text(text = "New releases", style = MaterialTheme.typography.titleMedium)
        HorizontalCarousel(carouselItems) {
            val albumRouteId = AlbumRouteId(it)
            navController.navigate(albumRouteId)
        }
    }
}
