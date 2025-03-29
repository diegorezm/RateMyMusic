package com.diegorezm.ratemymusic.modules.auth.use_cases

import com.diegorezm.ratemymusic.modules.auth.models.AuthDTO
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo

suspend fun signUpUseCase(dto: AuthDTO, auth: Auth): Result<UserInfo?> {
    return try {
        val user = auth.signUpWith(Email) {
            email = dto.email
            password = dto.password
        }
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }
}