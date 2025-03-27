package com.diegorezm.ratemymusic.presentation.followers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FollowersScreen(showFollowBtn: Boolean = false, followersCountViewModel: FollowersViewModel) {
    val followersCount by followersCountViewModel.followersCount.collectAsState()
    val followingCount by followersCountViewModel.followingCount.collectAsState()
    val isFollowing by followersCountViewModel.isFollowing.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Text(text = "Followers: $followersCount")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Following: $followingCount")
        }
    }
    if (showFollowBtn) {
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (isFollowing) {
                    followersCountViewModel.unfollow()
                } else {
                    followersCountViewModel.follow()
                }
            },
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(if (isFollowing) "Unfollow" else "Follow")
        }
    }

}

