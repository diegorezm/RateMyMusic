package com.diegorezm.ratemymusic.presentation.auth.sign_up

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.auth.models.AuthDTO
import com.diegorezm.ratemymusic.modules.auth.use_cases.signInUseCase
import com.diegorezm.ratemymusic.modules.auth.use_cases.signUpUseCase
import com.diegorezm.ratemymusic.presentation.auth.AuthResult
import com.diegorezm.ratemymusic.presentation.auth.AuthViewModel
import com.diegorezm.ratemymusic.presentation.auth.GoogleAuthUiClient
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val googleAuthClient: GoogleAuthUiClient
) : AuthViewModel(googleAuthClient) {
    private val tag = "SignUpViewModel"

    fun signUpWithEmailAndPassword(name: String, email: String, password: String) {
        val dto = AuthDTO(email, password)
        viewModelScope.launch {
            signUpUseCase(dto).onSuccess {
                _authState.value = AuthResult.Success

                handleProfileCreation(
                    name,
                    email,
                    photoUrl = null
                )
                
                signInUseCase(dto).onSuccess {
                    _authState.value = AuthResult.Success
                }.onFailure {
                    Log.e(tag, it.message ?: "Unknown error")
                    _authState.value = AuthResult.Error("Something went wrong while signing in.")
                }


            }.onFailure {
                Log.e(tag, it.message ?: "Unknown error")
                _authState.value = AuthResult.Error("Something went wrong while signing up.")
            }
        }
    }
}