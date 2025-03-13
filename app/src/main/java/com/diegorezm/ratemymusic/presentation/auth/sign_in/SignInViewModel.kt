package com.diegorezm.ratemymusic.presentation.auth.sign_in

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.auth.models.AuthDTO
import com.diegorezm.ratemymusic.modules.auth.use_cases.signInUseCase
import com.diegorezm.ratemymusic.modules.profiles.data_access.ProfileRepository
import com.diegorezm.ratemymusic.presentation.auth.AuthResult
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
                _authState.value = AuthResult.Success
            }.onFailure {
                Log.e(tag, it.message ?: "Unknown error")
                _authState.value = AuthResult.Error("Something went wrong while signing in.")
            }
        }
    }
}
