package com.diegorezm.ratemymusic.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.presentation.components.ThemedCard

data class SearchItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageURL: String
)

@Composable
fun SearchList(list: List<SearchItem>, onNavClick: (String) -> Unit) {
    LazyColumn {
        items(list) { item ->
            SearchItem(item, onNavClick)
        }
    }
}

@Composable
fun SearchItem(item: SearchItem, onNavClick: (String) -> Unit) {
    ThemedCard {
        TextButton(onClick = {
            onNavClick(item.id)
        }) {
            Row {
                AsyncImage(
                    model = item.imageURL,
                    contentDescription = "${item.title} picture",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(60.dp)
                        .width(60.dp)
                )
                Column(
                    Modifier.weight(1f)
                ) {
                    Text(
                        text = item.title,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                }
            }
        }
    }
}