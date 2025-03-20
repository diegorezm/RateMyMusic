package com.diegorezm.ratemymusic.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.presentation.home.HomeScreen
import com.diegorezm.ratemymusic.presentation.profile.ProfileScreen
import com.diegorezm.ratemymusic.presentation.profile.ProfileViewModel
import com.diegorezm.ratemymusic.presentation.search.SearchScreen
import com.diegorezm.ratemymusic.presentation.search.SearchViewModel

@Composable
fun MainScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    searchViewModel: SearchViewModel
) {
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
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedIndex) {
                0 -> HomeScreen()
                1 -> SearchScreen(
                    viewModel = searchViewModel
                )

                2 -> ProfileScreen(
                    navController = navController,
                    viewModel = profileViewModel
                )
            }
        }
    }
}