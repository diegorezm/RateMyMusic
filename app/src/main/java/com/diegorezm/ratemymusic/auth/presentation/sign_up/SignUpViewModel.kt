package com.diegorezm.ratemymusic.auth.presentation.sign_up

import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.auth.data.dto.SignUpDTO
import com.diegorezm.ratemymusic.auth.domain.AuthRepository
import com.diegorezm.ratemymusic.auth.presentation.AuthViewModel
import com.diegorezm.ratemymusic.auth.presentation.toUiText
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepository: AuthRepository
) : AuthViewModel(authRepository) {

    fun onSignUp(
        name: String,
        email: String,
        password: String
    ) {
        _authState.value = _authState.value.copy(
            isLoading = true
        )

        val signUpDTO = SignUpDTO(
            name = name,
            email = email,
            password = password
        )

        viewModelScope.launch {
            authRepository.signUp(signUpDTO)
                .onSuccess {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        isSuccessful = true
                    )
                }
                .onError {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = it,
                        errorMessage = it.toUiText()
                    )
                }
        }

    }
}