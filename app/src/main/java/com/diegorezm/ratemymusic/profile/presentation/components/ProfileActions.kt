package com.diegorezm.ratemymusic.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.profile.presentation.ProfileScreenActions

@Composable
fun ProfileActions(isFollowing: Boolean, onAction: (ProfileScreenActions) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            if (isFollowing) {
                onAction(ProfileScreenActions.OnUnfollowClick)
            } else {
                onAction(ProfileScreenActions.OnFollowClick)
            }
        }, shape = MaterialTheme.shapes.medium) {
            if (isFollowing) {
                Text(stringResource(R.string.unfollow))
            } else {
                Text(stringResource(R.string.follow))
            }
        }
    }
}