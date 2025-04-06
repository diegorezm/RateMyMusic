package com.diegorezm.ratemymusic.auth.presentation

import com.diegorezm.ratemymusic.auth.domain.AuthError
import com.diegorezm.ratemymusic.core.presentation.UiText

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccessful: Boolean = false,
    val error: AuthError? = null,
    val errorMessage: UiText? = null
)
