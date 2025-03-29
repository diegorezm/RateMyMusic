package com.diegorezm.ratemymusic.presentation.auth.sign_up

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.MainRoutes
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.SignInRouteId
import com.diegorezm.ratemymusic.presentation.auth.AuthState
import com.diegorezm.ratemymusic.presentation.auth.components.GoogleSignInButton
import com.diegorezm.ratemymusic.presentation.auth.components.PasswordTextInput
import com.diegorezm.ratemymusic.presentation.components.Separator

@Composable
fun SignUpScreen(navController: NavController, viewModel: SignUpViewModel) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = context.getString(R.string.sign_up_page_title),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

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
                    viewModel.signUpWithEmailAndPassword(name, email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(context.getString(R.string.sign_up_btn))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Separator(text = context.getString(R.string.separator_default_text))

            Spacer(modifier = Modifier.height(16.dp))

            GoogleSignInButton(
                text = context.getString(R.string.sign_in_with_google),
                onClick = {
                    viewModel.signInWithGoogle(it)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    navController.navigate(SignInRouteId)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(context.getString(R.string.already_have_account))
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (authState) {
                is AuthState.Loading -> CircularProgressIndicator()
                is AuthState.Success -> {
                    navController.navigate(MainRoutes.Home.route)
                    email = ""
                    password = ""
                }

                is AuthState.Error -> Text(
                    text = (authState as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )

                else -> {}
            }
        }
    }
}