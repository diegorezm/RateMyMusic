package com.diegorezm.ratemymusic.presentation.reviews

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review
import com.diegorezm.ratemymusic.presentation.components.Separator
import com.diegorezm.ratemymusic.utils.formatFirebaseTimestamp
import com.google.firebase.auth.FirebaseUser


@Composable
fun ReviewList(reviews: List<Review>, user: FirebaseUser, reviewsViewModel: ReviewsViewModel) {
    if (reviews.isEmpty()) {
        Text(text = "No reviews yet")
    } else {
        reviews.forEach {
            ReviewItem(it, user, onEdit = {
            }, onDelete = {
                reviewsViewModel.removeReview(it)
            })
            Separator()
        }
    }
}

@Composable
fun ReviewItem(
    review: Review,
    user: FirebaseUser,
    onEdit: (Review) -> Unit,
    onDelete: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = review.content,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = formatFirebaseTimestamp(review.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (user.uid == review.reviewerId) {
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
                                    Icons.Default.Edit,
                                    contentDescription = "Edit this review"
                                )
                            },
                            text = {
                                Text(
                                    text = "Edit",
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            },
                            onClick = {
                                expanded = false
                                onEdit(review)
                            }
                        )

                        Separator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        )

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