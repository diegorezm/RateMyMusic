package com.diegorezm.ratemymusic.core.presentation

import com.diegorezm.ratemymusic.R
import com.diegorezm.ratemymusic.core.domain.DataError

fun DataError.toUiText(): UiText {
    val stringRes = when (this) {
        DataError.Local.DISK_FULL -> R.string.error_disk_full
        DataError.Local.UNKNOWN -> R.string.error_local_unknown
        DataError.Remote.REQUEST_TIMEOUT -> R.string.error_request_timeout
        DataError.Remote.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
        DataError.Remote.NO_INTERNET -> R.string.error_no_internet
        DataError.Remote.SERVER -> R.string.error_server
        DataError.Remote.SERIALIZATION -> R.string.error_serialization
        DataError.Remote.UNKNOWN -> R.string.error_remote_unknown
        DataError.Remote.BAD_REQUEST -> R.string.error_bad_request
        DataError.Remote.UNAUTHORIZED -> R.string.error_unauthorized
        DataError.Remote.FORBIDDEN -> R.string.error_forbidden
        DataError.Remote.NOT_FOUND -> R.string.error_not_found
        DataError.Remote.CONFLICT -> R.string.error_conflict
        DataError.Remote.UNPROCESSABLE_ENTITY -> R.string.error_unprocessable_entity
        DataError.Remote.DATABASE_ERROR -> R.string.error_database
    }

    return UiText.StringResourceId(stringRes)
}