package com.diegorezm.ratemymusic.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegorezm.ratemymusic.presentation.components.BottomDrawer

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var showModal by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                showModal = true
            }
        ) {
            Text("Open modal")
        }

        BottomDrawer(showModal, onDismiss = {
            showModal = false
        }) {
            Text("Modal content")
        }
    }
}