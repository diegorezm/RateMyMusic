package com.diegorezm.ratemymusic.auth.presentation.sign_up

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.diegorezm.ratemymusic.auth.data.dto.SignUpDTO
import com.diegorezm.ratemymusic.auth.domain.AuthRepository
import com.diegorezm.ratemymusic.auth.presentation.AuthViewModel
import com.diegorezm.ratemymusic.auth.presentation.toUiText
import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.onError
import com.diegorezm.ratemymusic.core.domain.onSuccess
import com.diegorezm.ratemymusic.core.presentation.toUiText
import com.diegorezm.ratemymusic.profile.data.dto.ProfileDTO
import com.diegorezm.ratemymusic.profile.domain.repositories.ProfileRepository
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepository: AuthRepository, profileRepository: ProfileRepository, auth: Auth
) : AuthViewModel(authRepository, profileRepository, auth) {

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

                    val profileDTO = ProfileDTO(
                        uid = user.id,
                        name = signUpDTO.name,
                        email = signUpDTO.email,
                        photoUrl = "",
                    )
                    createProfile(profileDTO)
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