package com.diegorezm.ratemymusic.profile.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.core.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.core.presentation.toUiText
import com.diegorezm.ratemymusic.profile.presentation.components.ProfileCard
import com.diegorezm.ratemymusic.profile.presentation.components.ProfileFollowersCount
import com.diegorezm.ratemymusic.user_favorites.presentation.UserFavoritesScreenRoot
import com.diegorezm.ratemymusic.user_favorites.presentation.UserFavoritesViewModel

@Composable
fun ProfileScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel,
    userFavoritesViewModel: UserFavoritesViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LazyColumn {
        item {
            ProfileScreen(modifier.padding(16.dp), state)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            UserFavoritesScreenRoot(
                viewModel = userFavoritesViewModel,
                navController = navController
            )
        }
    }
}

@Composable
private fun ProfileScreen(
    modifier: Modifier = Modifier,
    state: ProfileState,
) {
    Column(
        modifier
    ) {
        if (state.profile != null) {
            ProfileCard(profile = state.profile)
            Spacer(modifier = Modifier.height(8.dp))
            ProfileFollowersCount(
                following = state.followingCount,
                followers = state.followersCount
            )
        }
        if (state.isLoading) {
            LoadingIndicator()
        }
        if (state.error != null) {
            Text(
                text = state.error.toUiText().asString(),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }

}