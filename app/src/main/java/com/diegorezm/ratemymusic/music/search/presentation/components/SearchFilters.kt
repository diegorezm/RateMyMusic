package com.diegorezm.ratemymusic.music.search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegorezm.ratemymusic.music.search.presentation.SearchType

@Composable
fun SearchFilters(
    modifier: Modifier = Modifier,
    currentFilter: SearchType,
    onFilterChange: (SearchType) -> Unit
) {
    val selectedIndex = SearchType.entries.indexOf(currentFilter)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SearchType.entries.forEachIndexed { index, type ->
            val isSelected = index == selectedIndex
            FilterChip(
                selected = isSelected,
                label = {
                    Text(type.name.lowercase())
                },
                onClick = {
                    onFilterChange(type)
                },
                modifier = Modifier.padding(end = 8.dp),
                colors = FilterChipDefaults.filterChipColors(
                    labelColor = MaterialTheme.colorScheme.onSurface,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                ),
                enabled = true,
                shape = MaterialTheme.shapes.medium,
            )
        }
    }

}

@Preview(name = "SearchFilters")
@Composable
private fun PreviewSearchFilters() {
    SearchFilters(
        currentFilter = SearchType.ALBUM,
        onFilterChange = {}
    )
}