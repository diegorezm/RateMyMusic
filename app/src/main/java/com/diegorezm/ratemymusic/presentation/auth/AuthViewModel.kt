package com.diegorezm.ratemymusic.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.domain.use_cases.checkIfProfileExistsUseCase
import com.diegorezm.ratemymusic.modules.profiles.domain.use_cases.createProfileUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class AuthViewModel(
    private val googleAuthClient: GoogleAuthUiClient,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    protected val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    init {
        observeAuthState()
    }

    protected open fun observeAuthState() {
        viewModelScope.launch {
            googleAuthClient.authState.collect { state ->
                when (state) {
                    is AuthState.Success -> {
                        val auth = Firebase.auth
                        val user = auth.currentUser

                        if (user != null) {
                            handleProfileCreation(
                                uid = user.uid.toString(),
                                name = user.displayName.toString(),
                                email = user.email.toString(),
                                photoUrl = user.photoUrl?.toString()
                            )
                        }
                    }

                    is AuthState.Error -> {
                        _authState.value = AuthState.Error(state.message)
                    }

                    else -> {
                        _authState.value = state
                    }
                }
            }
        }
    }

    protected fun handleProfileCreation(
        uid: String,
        name: String,
        email: String,
        photoUrl: String?
    ) {
        viewModelScope.launch {
            val profileExists = checkIfProfileExistsUseCase(uid, profileRepository)
            if (!profileExists) {
                createProfileUseCase(
                    uid,
                    name,
                    email,
                    photoUrl,
                    profileRepository
                ).onSuccess {
                    _authState.value = AuthState.Success
                }.onFailure {
                    _authState.value = AuthState.Error("Failed to create profile.")
                }
            } else {
                _authState.value = AuthState.Success
            }
        }
    }

    open fun signInWithGoogle() {
        googleAuthClient.signIn()
    }
}