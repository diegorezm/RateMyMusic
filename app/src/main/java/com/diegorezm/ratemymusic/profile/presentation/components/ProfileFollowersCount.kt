package com.diegorezm.ratemymusic.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegorezm.ratemymusic.core.presentation.theme.RateMyMusicTheme

@Composable
fun ProfileFollowersCount(
    modifier: Modifier = Modifier,
    following: Int,
    followers: Int
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        TextButton(onClick = {}) {
            Row {
                Text("Following: ")
                Text(following.toString())
            }

        }
        Spacer(Modifier.width(12.dp))
        TextButton(onClick = {}) {
            Row {
                Text("Followers: ")
                Text(followers.toString())
            }

        }
    }
}

@Composable
@Preview(showBackground = true, name = "ProfileFollowersCount")
private fun ProfileFollowersCountPreview() {
    RateMyMusicTheme {
        ProfileFollowersCount(Modifier, 12, 12)
    }
}