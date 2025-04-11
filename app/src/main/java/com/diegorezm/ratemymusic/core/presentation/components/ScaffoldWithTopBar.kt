package com.diegorezm.ratemymusic.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    title: String = "Page",
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {

                    val imageVector =
                        ImageVector.vectorResource(R.drawable.baseline_keyboard_arrow_left_24)

                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = imageVector,
                            contentDescription = "Back"
                        )
                    }
                }
            )

        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            content()
        }
    }
}

@Preview(name = "ScaffoldWithTopBar")
@Composable
private fun PreviewScaffoldWithTopBar() {
    val navController = NavController(LocalContext.current)
    ScaffoldWithTopBar(onBackClick = { navController.popBackStack() }) {}
}
