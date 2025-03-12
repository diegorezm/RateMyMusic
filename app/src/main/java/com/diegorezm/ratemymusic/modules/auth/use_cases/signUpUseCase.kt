package com.diegorezm.ratemymusic.modules.auth.use_cases

import com.diegorezm.ratemymusic.modules.auth.models.AuthDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

suspend fun signUpUseCase(dto: AuthDTO): Result<FirebaseUser?> {
    return try {
        val auth = FirebaseAuth.getInstance()
        val res = auth.createUserWithEmailAndPassword(dto.email, dto.password).await()
        Result.success(res.user)
    } catch (e: Exception) {
        Result.failure(e)
    }
}