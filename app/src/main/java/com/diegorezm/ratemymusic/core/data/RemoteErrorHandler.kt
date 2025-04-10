package com.diegorezm.ratemymusic.core.data

import com.diegorezm.ratemymusic.core.domain.DataError
import io.github.jan.supabase.postgrest.exception.PostgrestRestException


object RemoteErrorHandler {
    fun handleRemoteException(statusCode: Int): DataError.Remote {

        return when (statusCode) {
            400 -> DataError.Remote.BAD_REQUEST
            401 -> DataError.Remote.UNAUTHORIZED
            403 -> DataError.Remote.FORBIDDEN
            404 -> DataError.Remote.NOT_FOUND
            409 -> DataError.Remote.CONFLICT
            422 -> DataError.Remote.UNPROCESSABLE_ENTITY
            else -> DataError.Remote.DATABASE_ERROR
        }

    }

    fun handlePostgrestException(e: Exception): DataError.Remote {
        if (e is PostgrestRestException) {
            return handleRemoteException(e.statusCode)
        }
        return handleGenericException()
    }

    fun handleGenericException(): DataError.Remote {
        return DataError.Remote.UNKNOWN
    }
}