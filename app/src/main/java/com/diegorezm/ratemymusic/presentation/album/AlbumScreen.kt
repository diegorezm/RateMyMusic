package com.diegorezm.ratemymusic.presentation.album

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.presentation.reviews.ReviewsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    navController: NavController,
    viewModel: AlbumViewModel,
    reviewsViewModel: ReviewsViewModel,
) {
    val album by viewModel.albumState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Album Details",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {

                    val imageVector =
                        ImageVector.vectorResource(R.drawable.baseline_keyboard_arrow_left_24)

                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = imageVector,
                            contentDescription = "Back"
                        )
                    }
                }
            )

        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                when (album) {
                    is AlbumState.Error -> {
                        Text(text = (album as AlbumState.Error).message)
                    }

                    AlbumState.Idle, AlbumState.Loading -> LoadingIndicator()

                    is AlbumState.Success -> {
                        val albumData = (album as AlbumState.Success).album
                        if (albumData == null) {
                            Text(text = "No album data available")
                        } else {
                            AlbumDetail(navController, albumData, viewModel, reviewsViewModel)
                        }
                    }
                }
            }
        }
    }
}