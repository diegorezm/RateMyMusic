package com.diegorezm.ratemymusic.music.search.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.core.presentation.components.ThemedCard
import com.diegorezm.ratemymusic.core.presentation.components.ThemedCardVariant
import com.diegorezm.ratemymusic.core.presentation.theme.RateMyMusicTheme

data class SearchItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageURL: String
)

@Composable
fun SearchListItem(
    modifier: Modifier = Modifier,
    item: SearchItem,
    onClick: (String) -> Unit = {}
) {
    ThemedCard(
        modifier = modifier,
        variant = ThemedCardVariant.Ghost
    ) {
        TextButton(onClick = {
            onClick(item.id)
        }) {
            Row {
                AsyncImage(
                    model = item.imageURL,
                    contentDescription = "${item.title} picture",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(60.dp)
                        .width(60.dp),
                    contentScale = ContentScale.Fit
                )
                Column(
                    Modifier.weight(1f)
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                    )

                }
            }
        }

    }
}

@Preview(name = "SearchListItem")
@Composable
private fun PreviewSearchListItem() {
    val item = SearchItem(
        id = "1",
        title = "Title",
        subtitle = "Subtitle",
        imageURL = "https://picsum.photos/200"
    )
    RateMyMusicTheme {
        SearchListItem(item = item)
    }

}