package com.diegorezm.ratemymusic.features.auth.use_cases

import com.diegorezm.ratemymusic.features.auth.models.AuthDTO
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

suspend fun signUpUseCase(dto: AuthDTO): Result<Unit> {
    return try {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(dto.email, dto.password).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}