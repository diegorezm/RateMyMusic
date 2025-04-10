package com.diegorezm.ratemymusic.reviews.presentation.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.core.presentation.components.ThemedCard
import com.diegorezm.ratemymusic.core.presentation.components.ThemedCardVariant
import com.diegorezm.ratemymusic.reviews.domain.Review
import com.diegorezm.ratemymusic.reviews.presentation.ReviewsScreenActions
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

    ThemedCard(modifier = modifier.fillMaxWidth(), variant = ThemedCardVariant.Outlined) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(photoUrl = review.profile.avatarUrl ?: "")

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    TextButton(
                        onClick = {
                            onAction(ReviewsScreenActions.OnProfileClick(review.profile.id))
                        },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.padding(0.dp)
                    ) {
                        Text(
                            text = review.profile.name,
                            color = MaterialTheme.colorScheme.secondary,
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


//
//@Preview(name = "ReviewListItem")
//@Composable
//private fun PreviewReviewListItem() {
//    ReviewListItem()
//}