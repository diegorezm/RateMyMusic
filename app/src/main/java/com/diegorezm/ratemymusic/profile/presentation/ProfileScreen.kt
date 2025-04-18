package com.diegorezm.ratemymusic.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.R
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
            ProfileScreen(modifier.padding(16.dp), state, onAction = {
                viewModel.onAction(it)
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
            ProfileCard(profile = state.profile)
            Spacer(modifier = Modifier.height(8.dp))

            ProfileFollowersCount(
                following = state.followingCount,
                followers = state.followersCount
            )

            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (!state.isCurrentUser) {
                    ProfileActions(isFollowing = state.isFollowing, onAction)
                } else {
                    Button(onClick = {
                        onAction(ProfileScreenActions.OnSignOutClick)
                    }) {
                        Text(stringResource(R.string.sign_out))
                    }
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