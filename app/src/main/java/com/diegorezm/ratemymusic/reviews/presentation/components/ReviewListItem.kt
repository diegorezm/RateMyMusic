package com.diegorezm.ratemymusic.reviews.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.core.presentation.components.ThemedCard
import com.diegorezm.ratemymusic.core.presentation.components.ThemedCardVariant
import com.diegorezm.ratemymusic.core.presentation.theme.RateMyMusicTheme
import com.diegorezm.ratemymusic.profile.domain.models.Profile
import com.diegorezm.ratemymusic.reviews.domain.Review
import com.diegorezm.ratemymusic.reviews.presentation.ReviewsScreenActions
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun ReviewListItem(
    modifier: Modifier = Modifier,
    review: Review,
    currentUserId: String,
    onAction: (ReviewsScreenActions) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    val tmz = TimeZone.currentSystemDefault()
    val createdAtFormat = review.createdAt.toLocalDateTime(tmz)
    val monthValue =
        if (createdAtFormat.month.value < 10) "0${createdAtFormat.month.value}" else createdAtFormat.month.value
    val date =
        "${createdAtFormat.dayOfMonth}/$monthValue/${createdAtFormat.year}, ${createdAtFormat.hour}:${createdAtFormat.minute}"

    ThemedCard(modifier = modifier.fillMaxWidth(), variant = ThemedCardVariant.Primary) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(photoUrl = review.profile.avatarUrl ?: "")

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {

                    Text(
                        text = review.profile.name,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.clickable {
                            onAction(ReviewsScreenActions.OnProfileClick(review.profile.id))
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${review.rating}/5",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))


                Text(
                    text = review.content,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = date,

                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            }

            if (currentUserId == review.profile.id) {
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete this review"
                                )
                            },
                            text = {
                                Text(
                                    text = "Delete",
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            },
                            onClick = {
                                expanded = false
                                onAction(ReviewsScreenActions.OnDeleteReview(review.id))
                            }
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun ProfileImage(photoUrl: String) {
    if (photoUrl.isNotBlank()) {
        AsyncImage(
            model = photoUrl,
            contentDescription = "Reviewer Profile",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.default_avatar),
            contentDescription = "Default Profile",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
    }
}


@Preview(name = "ReviewListItem", showBackground = true)
@Composable
private fun PreviewReviewListItem() {
    val profile = Profile(
        id = "",
        name = "person",
        email = "person@email.com",
        avatarUrl = null,
        createdAt = ""
    )
    val review = Review(
        id = "",
        content = "This is me",
        entityId = "",
        entity = "",
        rating = 2,
        profile = profile,
        createdAt = Instant.fromEpochMilliseconds(System.currentTimeMillis())
    )
    val currentUserId = ""
    val onAction: (ReviewsScreenActions) -> Unit = {}

    RateMyMusicTheme {
        ReviewListItem(
            modifier = Modifier,
            review = review,
            currentUserId = currentUserId,
            onAction = onAction
        )
    }

}