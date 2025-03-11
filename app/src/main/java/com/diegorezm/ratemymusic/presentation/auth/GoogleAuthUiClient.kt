package com.diegorezm.ratemymusic.presentation.auth

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.diegorezm.ratemymusic.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

class GoogleAuthUiClient(private val context: Context) {
    private val auth = Firebase.auth
    private val tag = "GoogleAuthUiClient"

    private val _authState = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val authState: StateFlow<AuthResult> = _authState

    fun signIn() {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(getGoogleId())
            .build()
        val credentialManager = CredentialManager.Companion.create(context)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                handleSignInResult(result.credential)
            } catch (e: GetCredentialException) {
                Log.e(tag, "Error: ${e.message}", e)
                _authState.value = AuthResult.Error("Google sign-in failed.")
            }
        }
    }

    fun handleSignInResult(credential: Credential) {
        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.Companion.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(tag, "Error: ${e.message}", e)
                        _authState.value = AuthResult.Error("Invalid Google ID Token")
                    }
                }
            }

            else -> {
                Log.e(tag, "Unexpected type of credential")
                _authState.value = AuthResult.Error("Unexpected credential type")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthResult.Success
                } else {
                    Log.w(tag, task.exception)
                    _authState.value =
                        AuthResult.Error("Authentication failed.")
                }
            }
    }

    private fun getGoogleId(): GetGoogleIdOption {
        val nonce = createNonce()
        return GetGoogleIdOption.Builder()
            .setAutoSelectEnabled(true)
            .setFilterByAuthorizedAccounts(false)
            .setNonce(nonce)
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .build()
    }

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}