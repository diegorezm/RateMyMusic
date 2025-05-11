package com.diegorezm.ratemymusic.settings.presentation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.app.Route
import com.diegorezm.ratemymusic.auth.domain.AuthRepository
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.core.presentation.components.ScaffoldWithTopBar
import com.diegorezm.ratemymusic.spotify_auth.presentation.ui.theme.RateMyMusicTheme
import kotlinx.coroutines.launch

@Composable
fun SettingsScreenRoot(
    authRepository: AuthRepository,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    SettingsPage(onAction = {
        when (it) {
            SettingsScreenActions.OnDeleteAccountClick -> {

            }

            SettingsScreenActions.OnSignOutClick -> {
                coroutineScope.launch {
                    authRepository.signOut().onSuccess {
                        navController.navigate(Route.SignIn)
                    }.onError {
                        Log.e("SettingsScreenRoot", "Error signing out\n $it")
                    }
                }
            }
        }
    }, onBackClick = {
        navController.navigateUp()
    })
}

@Composable
private fun SettingsPage(
    onAction: (SettingsScreenActions) -> Unit,
    onBackClick: () -> Unit
) {
    ScaffoldWithTopBar(
        onBackClick = {
            onBackClick()
        },
        title = stringResource(R.string.settings_page_title)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextButton(
                onClick = {
                    onAction(SettingsScreenActions.OnSignOutClick)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.sign_out))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsPagePreview() {
    RateMyMusicTheme {
        SettingsPage(
            onAction = {},
            onBackClick = {}
        )
    }
}
