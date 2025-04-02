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
            _authState.value = AuthState.Loading

            val dto = AuthDTO(email, password)

            runCatching {
                signUpUseCase(dto, auth).getOrThrow()
            }.mapCatching {
                val userInfo = auth.currentUserOrNull() ?: throw Exception("User not found")
                handleProfileCreation(
                    uid = userInfo.id,
                    name = name,
                    email = userInfo.email.toString(),
                    photoUrl = ""
                ).getOrThrow()
            }.mapCatching {
                signInUseCase(dto, auth).getOrThrow()
            }.onSuccess {
                _authState.value = AuthState.Success
            }.onFailure { e ->
                Log.e(tag, e.message ?: "Unknown error", e)
                _authState.value = AuthState.Error("Something went wrong.")
            }
        }
    }

}