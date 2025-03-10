package com.diegorezm.ratemymusic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.diegorezm.ratemymusic.pages.SignInPage
import com.diegorezm.ratemymusic.pages.SignUpPage


@Composable
fun AuthScreen(authManager: AuthManager) {
    var isSignUp by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (isSignUp) {
            SignUpPage(
                onNavigateToSignIn = {
                    isSignUp = false
                }
            )
        } else {
            SignInPage(
                onNavigateToSignUp = {
                    isSignUp = true
                }
            )
        }

    }

}