package com.diegorezm.ratemymusic.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.SignInRouteId
import com.diegorezm.ratemymusic.modules.auth.use_cases.signOutUseCase

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel,
    navController: NavController,
) {
    val profileState by viewModel.profileState.collectAsState()
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            when (profileState) {
                is ProfileState.Success -> {
                    val profile = (profileState as ProfileState.Success).profile
                    Image(
                        painter = painterResource(id = R.drawable.default_avatar),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = profile?.name ?: "Error",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = profile?.email ?: "Error",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                }

                is ProfileState.Error -> {
                    Text(
                        text = (profileState as ProfileState.Error).message,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }

                is ProfileState.Loading -> {
                    CircularProgressIndicator()
                }

                ProfileState.Idle -> {
                    CircularProgressIndicator()
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.handleSpotifyAuthBtn(context)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Spotify", color = Color.White)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    signOutUseCase().onSuccess {
                        navController.navigate(SignInRouteId) {
                            popUpTo(SignInRouteId) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Sign Out", color = Color.White)
            }
        }
    }
}