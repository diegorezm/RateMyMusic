package com.diegorezm.ratemymusic.presentation.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.BuildConfig
import com.diegorezm.ratemymusic.modules.profiles.data.models.ProfileDTO
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.domain.use_cases.createProfileUseCase
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

open class AuthViewModel(
    private val profileRepository: ProfileRepository,
    private val auth: Auth

) : ViewModel() {
    protected val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun signInWithGoogle(context: Context) {
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
        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(request = request, context = context)
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(result.credential.data)
                val googleIdToken = googleIdTokenCredential.idToken

                auth.signInWith(IDToken) {
                    idToken = googleIdToken
                    provider = Google
                    nonce = rawNonce
                }

                val u = auth.currentUserOrNull()
                if (u != null) {
                    val userMetadata = u.userMetadata
                    val name = userMetadata?.get("full_name").toString().removeSurrounding("\"")
                    val photoUrl =
                        userMetadata?.get("avatar_url").toString().removeSurrounding("\"")

                    handleProfileCreation(
                        uid = u.id,
                        name = name,
                        email = u.email ?: "",
                        photoUrl = photoUrl
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    protected fun handleProfileCreation(
        uid: String,
        name: String,
        email: String,
        photoUrl: String?
    ) {
        viewModelScope.launch {
            val profileDTO = ProfileDTO(
                uid = uid,
                name = name,
                email = email,
                photoUrl = photoUrl ?: ""
            )
            createProfileUseCase(
                profileDTO,
                profileRepository
            ).onSuccess {
                _authState.value = AuthState.Success
            }.onFailure {
                _authState.value = AuthState.Error("Failed to create profile.")
            }
        }
    }

}