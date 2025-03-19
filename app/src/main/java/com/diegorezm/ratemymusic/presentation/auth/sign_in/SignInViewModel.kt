package com.diegorezm.ratemymusic.presentation.auth.sign_in

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.auth.models.AuthDTO
import com.diegorezm.ratemymusic.modules.auth.use_cases.signInUseCase
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.presentation.auth.AuthState
import com.diegorezm.ratemymusic.presentation.auth.AuthViewModel
import com.diegorezm.ratemymusic.presentation.auth.GoogleAuthUiClient
import kotlinx.coroutines.launch

class SignInViewModel(
    private val googleAuthClient: GoogleAuthUiClient,
    private val profileRepository: ProfileRepository
) : AuthViewModel(googleAuthClient, profileRepository) {
    private val tag = "SignInViewModel"

    fun signInWithEmailAndPassword(email: String, password: String) {
        val dto = AuthDTO(email, password)
        viewModelScope.launch {
            signInUseCase(dto).onSuccess {
                _authState.value = AuthState.Success
            }.onFailure {
                Log.e(tag, it.message ?: "Unknown error")
                _authState.value = AuthState.Error("Something went wrong while signing in.")
            }
        }
    }
}
