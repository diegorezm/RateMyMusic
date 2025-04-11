package com.diegorezm.ratemymusic.auth.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.auth.domain.AuthRepository
import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.core.presentation.toUiText
import com.diegorezm.ratemymusic.profile.data.dto.ProfileDTO
import com.diegorezm.ratemymusic.profile.domain.repositories.ProfileRepository
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class AuthViewModel(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    protected val auth: Auth
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
                        isLoading = false
                    )
                    val user = auth.currentUserOrNull()
                    if (user == null) {
                        Log.e("AuthViewModel", "User is null")
                        _authState.value = _authState.value.copy(
                            isLoading = false,
                            errorMessage = DataError.Remote.UNKNOWN.toUiText()
                        )
                        return@launch
                    }
                    val userMetadata = user.userMetadata
                    val name = userMetadata?.get("full_name").toString().removeSurrounding("\"")
                    val photoUrl =
                        userMetadata?.get("avatar_url").toString().removeSurrounding("\"")
                    val id = user.id
                    val email = user.email.toString()
                    val profile = ProfileDTO(
                        name = name,
                        photoUrl = photoUrl,
                        uid = id,
                        email = email,
                    )
                    createProfile(profile)
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

    protected fun createProfile(profileDTO: ProfileDTO) {


        viewModelScope.launch {
            profileRepository.checkIfProfileExists(profileDTO.uid).onSuccess {
                if (it) {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        isSuccessful = true
                    )
                } else {
                    profileRepository.create(profileDTO).onSuccess {
                        _authState.value = _authState.value.copy(
                            isLoading = false,
                            isSuccessful = true
                        )
                    }.onError {
                        _authState.value = _authState.value.copy(
                            isLoading = false,
                            errorMessage = it.toUiText()
                        )
                    }
                }
            }

        }
    }
}