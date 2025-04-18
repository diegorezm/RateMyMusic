package com.diegorezm.ratemymusic.auth.domain

import com.diegorezm.ratemymusic.core.domain.Error

sealed class AuthError : Error {
    object InvalidCredentials : AuthError()
    object UserAlreadyExists : AuthError()
    object UserNotFound : AuthError()
    object WrongPassword : AuthError()
    object NetworkError : AuthError()
    object UnknownError : AuthError()
    object UserNotLoggedIn : AuthError()
}
