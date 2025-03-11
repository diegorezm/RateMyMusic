package com.diegorezm.ratemymusic.presentation.auth.sign_in

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diegorezm.ratemymusic.MainRouteId
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.SignUpRouteId
import com.diegorezm.ratemymusic.presentation.auth.AuthResult
import com.diegorezm.ratemymusic.presentation.auth.GoogleSignInButton
import com.diegorezm.ratemymusic.presentation.auth.PasswordTextInput
import com.diegorezm.ratemymusic.presentation.common.components.Separator

@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    navController: NavController
) {
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
                text = context.getString(R.string.sign_in_page_title),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                    viewModel.signInWithEmailAndPassword(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(context.getString(R.string.sign_in_btn))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Separator(context.getString(R.string.separator_default_text))

            Spacer(modifier = Modifier.height(16.dp))

            GoogleSignInButton(
                text = context.getString(R.string.sign_in_with_google),
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    navController.navigate(SignUpRouteId)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(context.getString(R.string.dont_have_account_yet))
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
                    text = (authState as AuthResult.Error).message,
                    color = MaterialTheme.colorScheme.error
                )

                else -> {}
            }
        }
    }
}
