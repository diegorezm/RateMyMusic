package com.diegorezm.ratemymusic.utils

import android.util.Log
import com.diegorezm.ratemymusic.modules.common.PublicException

suspend fun <T> handleResult(tag: String = "Unexpected", x: suspend () -> T): Result<T> {
    return try {
        Result.success(x())
    } catch (e: PublicException) {
        Result.failure(e)
    } catch (e: Exception) {
        Log.e(tag, "An unexpected error occurred", e)
        Result.failure(PublicException("An unexpected error occurred"))
    }
}