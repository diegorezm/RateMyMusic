package com.diegorezm.ratemymusic.auth.presentation

import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.auth.domain.AuthError
import com.diegorezm.ratemymusic.core.presentation.UiText

fun AuthError.toUiText(): UiText {
    val stringRes = when (this) {
        AuthError.InvalidEmail -> R.string.error_invalid_email
        AuthError.InvalidPassword -> R.string.error_invalid_password
        AuthError.UserAlreadyExists -> R.string.error_user_already_exists
        AuthError.UserNotFound -> R.string.error_user_not_found
        AuthError.WrongPassword -> R.string.error_wrong_password
        AuthError.NetworkError -> R.string.error_network
        AuthError.UnknownError -> R.string.error_unknown
        AuthError.UserNotLoggedIn -> R.string.error_user_not_logged_in
    }
    return UiText.StringResourceId(stringRes)
}
