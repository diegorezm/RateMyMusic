package com.diegorezm.ratemymusic.presentation.auth.sign_up

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.auth.models.AuthDTO
import com.diegorezm.ratemymusic.modules.auth.use_cases.signInUseCase
import com.diegorezm.ratemymusic.modules.auth.use_cases.signUpUseCase
import com.diegorezm.ratemymusic.modules.profiles.data_access.ProfileRepository
import com.diegorezm.ratemymusic.presentation.auth.AuthResult
import com.diegorezm.ratemymusic.presentation.auth.AuthViewModel
import com.diegorezm.ratemymusic.presentation.auth.GoogleAuthUiClient
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val googleAuthClient: GoogleAuthUiClient,
    private val profileRepository: ProfileRepository
) : AuthViewModel(googleAuthClient, profileRepository) {
    private val tag = "SignUpViewModel"

    fun signUpWithEmailAndPassword(name: String, email: String, password: String) {
        viewModelScope.launch {
            val dto = AuthDTO(email, password)
            signUpUseCase(dto).onSuccess {
                _authState.value = AuthResult.Success

                handleProfileCreation(
                    uid = it?.uid.toString(),
                    name = name,
                    email = it?.email.toString(),
                    photoUrl = it?.photoUrl.toString(),
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