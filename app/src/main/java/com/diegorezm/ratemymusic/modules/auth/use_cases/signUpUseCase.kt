package com.diegorezm.ratemymusic.modules.auth.use_cases

import com.diegorezm.ratemymusic.modules.auth.models.AuthDTO
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email

suspend fun signUpUseCase(dto: AuthDTO, auth: Auth): Result<Unit> {
    return try {
        auth.signUpWith(Email) {
            email = dto.email
            password = dto.password
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}