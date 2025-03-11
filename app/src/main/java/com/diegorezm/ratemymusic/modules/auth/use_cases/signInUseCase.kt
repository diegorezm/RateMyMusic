package com.diegorezm.ratemymusic.modules.auth.use_cases

import com.diegorezm.ratemymusic.modules.auth.models.AuthDTO
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

suspend fun signInUseCase(dto: AuthDTO): Result<Unit> {
    return try {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(dto.email, dto.password).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}