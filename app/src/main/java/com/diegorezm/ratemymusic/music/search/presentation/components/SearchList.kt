package com.diegorezm.ratemymusic.music.search.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.diegorezm.ratemymusic.R

@Composable
fun SearchList(
    modifier: Modifier = Modifier,
    items: List<SearchItem>,
    onClick: (String) -> Unit = {}
) {
    if (items.isEmpty()) {
        Text(text = stringResource(R.string.nothing_to_see_here))
        return
    }
    LazyColumn(
        modifier = modifier
    ) {
        items(items) { item ->
            SearchListItem(item = item, onClick = onClick)
        }
    }
}