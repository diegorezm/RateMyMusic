package com.diegorezm.ratemymusic.presentation.auth.sign_in

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.auth.models.AuthDTO
import com.diegorezm.ratemymusic.modules.auth.use_cases.signInUseCase
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.presentation.auth.AuthState
import com.diegorezm.ratemymusic.presentation.auth.AuthViewModel
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.launch

class SignInViewModel(
    profileRepository: ProfileRepository,
    private val auth: Auth
) : AuthViewModel(profileRepository, auth) {
    private val tag = "SignInViewModel"

    fun signInWithEmailAndPassword(email: String, password: String) {
        val dto = AuthDTO(email, password)
        viewModelScope.launch {
            signInUseCase(dto, auth).onSuccess {
                _authState.value = AuthState.Success
            }.onFailure {
                Log.e(tag, it.message ?: "Unknown error")
                _authState.value = AuthState.Error("Something went wrong while signing in.")
            }
        }
    }
}
