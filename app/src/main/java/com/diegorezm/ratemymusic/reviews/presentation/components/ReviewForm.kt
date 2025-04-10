package com.diegorezm.ratemymusic.reviews.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.core.presentation.theme.RateMyMusicTheme

@Composable
fun ReviewForm(onSubmit: (newReview: String, stars: Int) -> Unit) {
    var newReview by remember { mutableStateOf("") }
    var stars by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.leave_a_review),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = newReview,
            onValueChange = { newReview = it },
            label = { Text(stringResource(R.string.write_your_review_hint)) },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(5) { index ->
                val starIndex = index + 1
                IconButton(
                    onClick = {
                        stars = starIndex
                    }
                ) {
                    val icon = if (starIndex <= stars) {
                        painterResource(R.drawable.ic_star_filled_24)
                    } else {
                        painterResource(R.drawable.ic_star_outline_24)
                    }

                    Image(
                        painter = icon,
                        contentDescription = "Star $starIndex",
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                onSubmit(newReview, stars)
                newReview = ""
                stars = 1
            },
            shape = MaterialTheme.shapes.medium,
            enabled = newReview.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.submit_review))
        }
    }
}

@Preview()
@Composable
private fun ReviewFormPreview() {
    RateMyMusicTheme {
        ReviewForm(onSubmit = { _, _ -> })
    }
}
