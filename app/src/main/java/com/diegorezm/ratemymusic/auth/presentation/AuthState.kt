package com.diegorezm.ratemymusic.auth.presentation

import com.diegorezm.ratemymusic.auth.domain.AuthError

data class AuthState(
    val isLoading: Boolean = false,
    val error: AuthError? = null
)
