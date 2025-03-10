package com.diegorezm.ratemymusic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diegorezm.ratemymusic.pages.HomePage
import com.diegorezm.ratemymusic.pages.ProfilePage
import com.diegorezm.ratemymusic.pages.SearchPage


@Composable
@Preview(showBackground = true)
fun MainScreen(modifier: Modifier = Modifier) {
    val navList = listOf(
        NavItem(label = "Home", icon = Icons.Default.Home),
        NavItem(label = "Search", icon = Icons.Default.Search),
        NavItem(label = "Profile", icon = Icons.Default.Person),
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                navList.forEachIndexed { idx, item ->
                    NavigationBarItem(
                        selected = selectedIndex == idx,
                        icon = {
                            Icon(imageVector = item.icon, contentDescription = "Icon")
                        },
                        label = {
                            Text(text = item.label)
                        },
                        onClick = {
                            selectedIndex = idx
                        },
                        colors = NavigationBarItemColors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            selectedIndicatorColor = MaterialTheme.colorScheme.secondary,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f),
                            unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f),
                            disabledIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    )


                }
            }
        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex)
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIdx: Int) {
    Box(modifier = modifier.padding(horizontal = 16.dp)) {
        when (selectedIdx) {
            0 -> HomePage()
            1 -> SearchPage()
            2 -> ProfilePage()
        }
    }

}

data class NavItem(
    val label: String,
    val icon: ImageVector
)