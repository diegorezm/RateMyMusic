package com.diegorezm.ratemymusic.presentation.profile.profiles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.presentation.components.ErrorMessage
import com.diegorezm.ratemymusic.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.presentation.components.ScaffoldWithTopBar
import com.diegorezm.ratemymusic.presentation.followers.FollowersViewModel
import com.diegorezm.ratemymusic.presentation.profile.ProfileCard
import com.diegorezm.ratemymusic.presentation.profile.ProfileState
import com.diegorezm.ratemymusic.presentation.user_favorites.UserFavoritesScreen
import com.diegorezm.ratemymusic.presentation.user_favorites.UserFavoritesViewModel

@Composable
fun ProfilesScreen(
    modifier: Modifier = Modifier,
    profilesViewModel: ProfilesViewModel,
    navController: NavController,
    favoritesViewModel: UserFavoritesViewModel,
    followersCountViewModel: FollowersViewModel
) {
    val profileState by profilesViewModel.profileState.collectAsState()
    val isCurrentUser by profilesViewModel.isCurrentUser.collectAsState()
    ScaffoldWithTopBar(
        navController = navController,
        title = "Profile"
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            when (profileState) {
                is ProfileState.Success -> {
                    val profile = (profileState as ProfileState.Success).profile
                    ProfileCard(
                        profile = profile,
                        isCurrentUser = isCurrentUser,
                        followersCountViewModel = followersCountViewModel
                    )
                }

                is ProfileState.Error -> {
                    val message = (profileState as ProfileState.Error).message
                    ErrorMessage(message)
                }

                ProfileState.Loading, ProfileState.Idle -> {
                    LoadingIndicator()
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            UserFavoritesScreen(navController, favoritesViewModel)
        }
    }

}