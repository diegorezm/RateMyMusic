package com.diegorezm.ratemymusic.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.auth.domain.AuthRepository
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    protected val _authState = MutableStateFlow(AuthState())
    val authSate = _authState.asStateFlow()

    fun onGoogleSignin(googleIdToken: String, rawNonce: String) {
        _authState.value = _authState.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            authRepository.signInWithGoogle(googleIdToken, rawNonce)
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