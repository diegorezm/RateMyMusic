package com.diegorezm.ratemymusic.auth.presentation.components

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.diegorezm.ratemymusic.R

@Composable
fun PrivacyPolicyButton(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val intent = remember {
        Intent(
            Intent.ACTION_VIEW,
            "https://diegorezm.github.io/RateMyMusic/privacy.html".toUri()
        )
    }

    TextButton(
        onClick = {
            context.startActivity(intent)
        },
        modifier = modifier
            .padding(4.dp)
    ) {
        Text(
            text = stringResource(R.string.privacy_policy),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }

}