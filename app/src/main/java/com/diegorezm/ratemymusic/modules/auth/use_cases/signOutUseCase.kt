package com.diegorezm.ratemymusic.features.auth.use_cases

import com.google.firebase.auth.FirebaseAuth

fun signOutUseCase(): Result<Unit> {
    return try {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}