package com.diegorezm.ratemymusic.presentation.reviews

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.diegorezm.ratemymusic.modules.profiles.domain.models.Profile
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewEntityType
import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review
import com.diegorezm.ratemymusic.modules.reviews.domain.models.ReviewWithProfile
import com.diegorezm.ratemymusic.presentation.components.Separator
import com.diegorezm.ratemymusic.ui.theme.RateMyMusicTheme
import com.diegorezm.ratemymusic.utils.formatFirebaseTimestamp


@Composable
fun ReviewList(
    reviews: List<ReviewWithProfile>,
    currentUserId: String = "",
    onClick: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    if (reviews.isEmpty()) {
        Text(text = "No reviews yet")
    } else {
        reviews.forEach {
            ReviewItem(it, currentUserId, onClick, onDelete)
            Separator()
        }
    }
}

@Composable
fun ReviewItem(
    reviewWithProfile: ReviewWithProfile,
    currentUserId: String,
    onClick: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val review = reviewWithProfile.review
    val profile = reviewWithProfile.profile
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(photoUrl = profile.photoUrl ?: "")

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    TextButton(
                        onClick = {
                            onClick(profile.uid)
                        },
                        contentPadding = PaddingValues(0.dp),

                        ) {
                        Text(
                            text = profile.name,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${review.rating}/5",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }


                Text(
                    text = review.content,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = formatFirebaseTimestamp(review.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (currentUserId == review.reviewerId) {
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
                                onDelete(review.id)
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
                .size(40.dp)
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


@Preview
@Composable
private fun ReviewItemPreview() {
    val review = Review(
        id = "1",
        reviewerId = "1",
        entityId = "1",
        entityType = ReviewEntityType.ALBUM,
        content = "This is a review",
    )

    val profile = Profile(
        name = "Duckworth",
        email = "duckworth.email",
        uid = "ads",
        photoUrl = null
    )

    val reviewWithProfile = ReviewWithProfile(review, profile)
    RateMyMusicTheme {
        ReviewItem(
            reviewWithProfile = reviewWithProfile,
            currentUserId = "a",
            onClick = {

            },
            onDelete = {}
        )
    }

}