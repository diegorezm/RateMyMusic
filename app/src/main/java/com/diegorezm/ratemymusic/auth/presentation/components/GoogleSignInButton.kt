package com.diegorezm.ratemymusic.auth.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.diegorezm.ratemymusic.BuildConfig
import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.spotify_auth.presentation.ui.theme.RateMyMusicTheme
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

@Composable
fun GoogleSignInButtonRoot(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String = "Sign in with Google",
    onSignInClicked: (googleIdToken: String, rawNonce: String) -> Unit,
    onError: (Exception) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val onClick: () -> Unit = {
        val credentialManager = CredentialManager.create(context)
        val rawNonce = UUID.randomUUID()
            .toString()
        val bytes = rawNonce.toString().toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }
        val googleIdOption =
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                .setNonce(hashedNonce)
                .build()
        val request: GetCredentialRequest =
            GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
        coroutineScope.launch {
            try {
                val result = credentialManager.getCredential(request = request, context = context)
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(result.credential.data)
                val googleIdToken = googleIdTokenCredential.idToken
                onSignInClicked(googleIdToken, rawNonce)
            } catch (e: Exception) {
                onError(e)
            }
        }

    }


    GoogleSignInButton(
        modifier = modifier,
        text = text,
        enabled = enabled,
        onSignInClicked = {
            onClick()
        }
    )
}

@Composable
private fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String = "Sign in with Google",
    onSignInClicked: () -> Unit = {}
) {

    OutlinedButton(
        onClick = {
            onSignInClicked()
        },
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_logo_48x48),
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, style = MaterialTheme.typography.bodyMedium)
        }
    }

}

@Preview(name = "GoogleSignInButton", showBackground = true)
@Composable
private fun PreviewGoogleSignInButton() {
    RateMyMusicTheme {
        GoogleSignInButton()
    }
}