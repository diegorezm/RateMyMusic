package com.diegorezm.ratemymusic.presentation.auth.sign_up

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.MainRouteId
import com.diegorezm.ratemymusic.SignInRouteId
import com.diegorezm.ratemymusic.presentation.auth.AuthResult
import com.diegorezm.ratemymusic.presentation.auth.GoogleSignInButton
import com.diegorezm.ratemymusic.presentation.auth.PasswordTextInput
import com.diegorezm.ratemymusic.presentation.common.components.Separator

@Composable
fun SignUpScreen(navController: NavController, viewModel: SignUpViewModel) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Sign Up", style = MaterialTheme.typography.titleLarge)

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
                    viewModel.signUpWithEmailAndPassword(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Sign Up")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Separator("OR")

            Spacer(modifier = Modifier.height(16.dp))

            GoogleSignInButton(text = "Sign up with Google", viewModel = viewModel)

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    navController.navigate(SignInRouteId)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Already have an account? Sign In")
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (authState) {
                is AuthResult.Loading -> CircularProgressIndicator()
                is AuthResult.Success -> {
                    navController.navigate(MainRouteId)
                    email = ""
                    password = ""
                }

                is AuthResult.Error -> Text(
                    text = "❌ ${(authState as AuthResult.Error).message}",
                    color = MaterialTheme.colorScheme.error
                )

                else -> {}
            }
        }
    }
}