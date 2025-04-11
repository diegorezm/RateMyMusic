package com.diegorezm.ratemymusic.auth.presentation.sign_in

import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.auth.data.dto.SignInDTO
import com.diegorezm.ratemymusic.auth.domain.AuthRepository
import com.diegorezm.ratemymusic.auth.presentation.AuthViewModel
import com.diegorezm.ratemymusic.auth.presentation.toUiText
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.profile.domain.repositories.ProfileRepository
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authRepository: AuthRepository, profileRepository: ProfileRepository, auth: Auth
) : AuthViewModel(authRepository, profileRepository, auth) {

    fun onSignIn(email: String, password: String) {
        val signInDTO = SignInDTO(
            email = email,
            password = password
        )

        _authState.value = _authState.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            authRepository.signIn(signInDTO).onSuccess {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    isSuccessful = true
                )
            }.onError {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = it,
                    errorMessage = it.toUiText()
                )
            }
        }
    }
}