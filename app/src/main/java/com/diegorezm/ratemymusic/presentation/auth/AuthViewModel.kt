package com.diegorezm.ratemymusic.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.modules.profiles.data_access.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.use_cases.checkIfProfileExistsUseCase
import com.diegorezm.ratemymusic.modules.profiles.use_cases.createProfileUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class AuthViewModel(
    private val googleAuthClient: GoogleAuthUiClient,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    protected val _authState = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val authState: StateFlow<AuthResult> = _authState

    init {
        observeAuthState()
    }

    protected open fun observeAuthState() {
        viewModelScope.launch {
            googleAuthClient.authState.collect { state ->
                when (state) {
                    is AuthResult.Success -> {
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

                    is AuthResult.Error -> {
                        _authState.value = AuthResult.Error(state.message)
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
                    _authState.value = AuthResult.Success
                }.onFailure {
                    _authState.value = AuthResult.Error("Failed to create profile.")
                }
            } else {
                _authState.value = AuthResult.Success
            }
        }
    }

    open fun signInWithGoogle() {
        googleAuthClient.signIn()
    }
}