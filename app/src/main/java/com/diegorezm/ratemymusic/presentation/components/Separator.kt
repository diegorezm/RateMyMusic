package com.diegorezm.ratemymusic.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Separator(modifier: Modifier = Modifier.fillMaxWidth(), text: String? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        HorizontalDivider(Modifier.weight(1f), 1.dp, MaterialTheme.colorScheme.onBackground)

        if (text != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text)
            Spacer(modifier = Modifier.width(8.dp))
        }

        HorizontalDivider(Modifier.weight(1f), 1.dp, MaterialTheme.colorScheme.onBackground)
    }
}
