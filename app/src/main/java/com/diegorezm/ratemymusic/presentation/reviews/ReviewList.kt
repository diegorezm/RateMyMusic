package com.diegorezm.ratemymusic.presentation.reviews

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review
import java.util.Locale


@Composable
fun ReviewList(reviews: List<Review>) {
    Column(modifier = Modifier.padding(16.dp)) {
        reviews.forEach { review ->
            ReviewItem(review)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(review.reviwerPhotoUrl)
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.reviwerName,
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = review.content,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = formatTimestamp(review.timestamp.toString()),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ProfileImage(photoUrl: String) {
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

fun formatTimestamp(dateString: String): String {
    return try {
        val originalFormat =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val targetFormat = SimpleDateFormat(
            "MMMM dd, yyyy",
            Locale.getDefault()
        )
        val date = originalFormat.parse(dateString)
        date?.let { targetFormat.format(it) }
            ?: dateString
    } catch (e: Exception) {
        Log.e("AlbumComponent", "Error formatting release date: $dateString", e)
        dateString
    }
}
