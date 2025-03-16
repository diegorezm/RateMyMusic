package com.diegorezm.ratemymusic.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.AlbumRouteId
import com.diegorezm.ratemymusic.RateMyMusicApp.Companion.appModule
import com.diegorezm.ratemymusic.di.AppModule
import com.diegorezm.ratemymusic.presentation.home.HomeScreen
import com.diegorezm.ratemymusic.presentation.profile.ProfileScreen
import com.diegorezm.ratemymusic.presentation.profile.ProfileViewModel
import com.diegorezm.ratemymusic.presentation.search.SearchScreen

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel) {
    val navList = listOf(
        NavItem(label = "Home", icon = Icons.Default.Home),
        NavItem(label = "Search", icon = Icons.Default.Search),
        NavItem(label = "Profile", icon = Icons.Default.Person),
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(navList, selectedIndex) { selectedIndex = it }
        }
    ) { innerPadding ->
        Button(
            modifier = Modifier.padding(innerPadding),
            onClick = {
                navController.navigate(
                    AlbumRouteId(
                        albumId = "4HTy9WFTYooRjE9giTmzAF"
                    )
                )
            }
        ) {
            Text("Go to album page")
        }

        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            selectedIndex,
            navController,
            appModule
        )
    }
}

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIdx: Int,
    navController: NavController,
    appModule: AppModule
) {
    val profileScreenViewModule = ProfileViewModel(appModule.profileRepository)

    Box(modifier = modifier.padding(horizontal = 16.dp)) {
        when (selectedIdx) {
            0 -> HomeScreen()
            1 -> SearchScreen()
            2 -> ProfileScreen(navController = navController, viewModel = profileScreenViewModule)
        }
    }
}