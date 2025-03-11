package com.diegorezm.ratemymusic.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.SignInRouteId
import com.diegorezm.ratemymusic.modules.auth.use_cases.signOutUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, navController: NavController) {
    val auth = Firebase.auth
    val user = auth.currentUser

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(text = "Welcome ${user?.displayName}")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = {
                signOutUseCase().onSuccess {
                    navController.navigate(SignInRouteId)
                }
            }
        ) {
            Text("Sign out")
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    val navController = NavController(LocalContext.current)
    ProfileScreen(navController = navController)
}