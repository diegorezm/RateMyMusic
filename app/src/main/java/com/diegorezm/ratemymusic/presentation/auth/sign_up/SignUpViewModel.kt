package com.diegorezm.ratemymusic.presentation.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.auth.models.AuthDTO
import com.diegorezm.ratemymusic.modules.auth.use_cases.signUpUseCase
import com.diegorezm.ratemymusic.presentation.auth.GoogleAuthUiClient
import com.diegorezm.ratemymusic.presentation.sign_in.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val googleAuthClient: GoogleAuthUiClient
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val authState: StateFlow<AuthResult> = _authState

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            googleAuthClient.authState.collect { state ->
                _authState.value = state
            }
        }
    }

    fun signUpWithEmailAndPaassword(email: String, password: String) {
        val dto = AuthDTO(email, password)
        viewModelScope.launch {
            signUpUseCase(dto).onSuccess {
                _authState.value = AuthResult.Success
            }.onFailure {
                _authState.value = AuthResult.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun signInWithGoogle() {
        googleAuthClient.signIn()
    }

}