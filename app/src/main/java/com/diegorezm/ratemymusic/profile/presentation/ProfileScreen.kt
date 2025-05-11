package com.diegorezm.ratemymusic.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.app.Route
import com.diegorezm.ratemymusic.core.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.core.presentation.toUiText
import com.diegorezm.ratemymusic.profile.presentation.components.ProfileActions
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
            ProfileScreen(modifier.padding(16.dp), state, onAction = { action ->
                when (action) {

                    ProfileScreenActions.OnSettingsClick -> {
                        navController.navigate(Route.Settings)
                    }

                    else -> viewModel.onAction(action)
                }

            })
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
    onAction: (ProfileScreenActions) -> Unit
) {
    Column(
        modifier
    ) {
        if (state.profile != null) {
            if (state.isCurrentUser) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {
                        onAction(ProfileScreenActions.OnSettingsClick)
                    }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            }

            ProfileCard(profile = state.profile)
            Spacer(modifier = Modifier.height(8.dp))

            ProfileFollowersCount(
                following = state.followingCount,
                followers = state.followersCount
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (!state.isCurrentUser) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    ProfileActions(isFollowing = state.isFollowing, onAction)
                }
            }
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