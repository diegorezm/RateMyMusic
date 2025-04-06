package com.diegorezm.ratemymusic.auth.presentation.sign_in

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.auth.presentation.AuthState
import com.diegorezm.ratemymusic.auth.presentation.components.GoogleSignInButtonRoot
import com.diegorezm.ratemymusic.auth.presentation.components.PasswordTextInput
import com.diegorezm.ratemymusic.core.presentation.components.Separator
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = koinViewModel(),
    onSuccessfulSignIn: () -> Unit,
    onNoAccountClick: () -> Unit
) {
    val state by viewModel.authSate.collectAsStateWithLifecycle()
    SignInScreen(modifier, state, onSignInClicked = { email, password ->
        viewModel.onSignIn(email, password)
    }, onGoogleSignInClicked = { googleId, rawNonce ->
        viewModel.onGoogleSignin(googleId, rawNonce)

    }, onSuccessfulSignIn, onNoAccountClick)
}

@Composable
private fun SignInScreen(
    modifier: Modifier = Modifier,
    state: AuthState = AuthState(),
    onSignInClicked: (
        email: String,
        password: String
    ) -> Unit,
    onGoogleSignInClicked: (String, String) -> Unit,
    onSuccessfulSignIn: () -> Unit = {},
    onNoAccountClick: () -> Unit = {}

) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = context.getString(R.string.sign_in_page_title),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                when {
                    state.errorMessage != null -> {
                        Text(
                            text = state.errorMessage.asString(),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    state.isSuccessful -> {
                        onSuccessfulSignIn()
                    }

                    else -> {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))


                        PasswordTextInput(
                            password = password,
                            onPasswordChange = { password = it }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                onSignInClicked(email, password)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            shape = MaterialTheme.shapes.medium,
                            enabled = password.length >= 6 && email.isNotBlank()
                        ) {
                            Text(context.getString(R.string.sign_in_btn))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Separator(text = context.getString(R.string.separator_default_text))

                        Spacer(modifier = Modifier.height(16.dp))

                        GoogleSignInButtonRoot(
                            text = context.getString(R.string.sign_in_with_google),
                            onSignInClicked = { googleId, rawNonce ->
                                onGoogleSignInClicked(googleId, rawNonce)
                            },
                            modifier = Modifier,
                            onError = {
                                Log.e("GoogleSignInButtonRoot", "Error: ${it.message}", it)
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        TextButton(
                            onClick = {
                                onNoAccountClick()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            shape = MaterialTheme.shapes.medium,
                        ) {
                            Text(context.getString(R.string.dont_have_account_yet))
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }


        }
    }

}

@Preview(name = "SignInScreen")
@Composable
private fun PreviewSignInScreen() {
    SignInScreen(onSignInClicked = { a, b -> }, onGoogleSignInClicked = { a, b -> })
}