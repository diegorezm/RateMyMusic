package com.diegorezm.ratemymusic.presentation.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.presentation.components.LoadingIndicator
import com.diegorezm.ratemymusic.presentation.user_favorites.UserFavoritesScreen
import com.diegorezm.ratemymusic.presentation.user_favorites.UserFavoritesViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel,
    favoritesViewModel: UserFavoritesViewModel
) {
    val profileState by viewModel.profileState.collectAsState()

    val cardColors =
        CardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = cardColors,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (profileState) {
                    is ProfileState.Success -> {
                        val profile = (profileState as ProfileState.Success).profile
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            AsyncImage(
                                model = profile?.photoUrl,
                                placeholder = painterResource(id = R.drawable.default_avatar),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = profile?.name ?: "Unknown",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = profile?.email ?: "Unknown",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )


                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    viewModel.signOut(navController)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text(text = "SIGN OUT")
                            }
                        }
                    }

                    is ProfileState.Error -> {
                        Text(
                            text = (profileState as ProfileState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }

                    ProfileState.Loading, ProfileState.Idle -> {
                        LoadingIndicator()
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        UserFavoritesScreen(navController, favoritesViewModel)
    }
}
