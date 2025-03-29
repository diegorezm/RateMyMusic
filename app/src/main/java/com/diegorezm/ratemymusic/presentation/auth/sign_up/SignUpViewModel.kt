package com.diegorezm.ratemymusic.presentation.auth.sign_up

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.auth.models.AuthDTO
import com.diegorezm.ratemymusic.modules.auth.use_cases.signInUseCase
import com.diegorezm.ratemymusic.modules.auth.use_cases.signUpUseCase
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.presentation.auth.AuthState
import com.diegorezm.ratemymusic.presentation.auth.AuthViewModel
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.launch

class SignUpViewModel(
    profileRepository: ProfileRepository,
    private val auth: Auth
) : AuthViewModel(profileRepository, auth) {
    private val tag = "SignUpViewModel"

    fun signUpWithEmailAndPassword(name: String, email: String, password: String) {
        viewModelScope.launch {
            val dto = AuthDTO(email, password)
            signUpUseCase(dto, auth).onSuccess {
                _authState.value = AuthState.Success

                handleProfileCreation(
                    uid = it?.id.toString(),
                    name = name,
                    email = it?.email.toString(),
                    photoUrl = "",
                )

                signInUseCase(dto, auth).onSuccess {
                    _authState.value = AuthState.Success
                }.onFailure {
                    Log.e(tag, it.message ?: "Unknown error")
                    _authState.value = AuthState.Error("Something went wrong while signing in.")
                }


            }.onFailure {
                Log.e(tag, it.message ?: "Unknown error")
                _authState.value = AuthState.Error("Something went wrong while signing up.")
            }
        }
    }
}