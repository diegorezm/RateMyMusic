package com.diegorezm.ratemymusic.auth.data.repositories

import android.util.Log
import com.diegorezm.ratemymusic.auth.data.dto.SignInDTO
import com.diegorezm.ratemymusic.auth.data.dto.SignUpDTO
import com.diegorezm.ratemymusic.auth.domain.AuthError
import com.diegorezm.ratemymusic.auth.domain.AuthRepository
import com.diegorezm.ratemymusic.core.domain.EmptyResult
import com.diegorezm.ratemymusic.core.domain.Result
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.exceptions.RestException


class DefaultAuthRepository(
    private val auth: Auth,
) : AuthRepository {
    override suspend fun signIn(signInDTO: SignInDTO): EmptyResult<AuthError> {
        return try {
            auth.signInWith(Email) {
                email = signInDTO.email
                password = signInDTO.password
            }
            Result.Success(Unit)
        } catch (e: RestException) {
            when (e.statusCode) {
                400 -> Result.Error(AuthError.InvalidEmail)
                401 -> Result.Error(AuthError.WrongPassword)
                404 -> Result.Error(AuthError.UserNotFound)
                else -> Result.Error(AuthError.UnknownError)
            }
            Result.Error(AuthError.NetworkError)
        }
    }

    override suspend fun signInWithGoogle(idToken: String, nonce: String): EmptyResult<AuthError> {
        return try {
            auth.signInWith(IDToken) {
                this.idToken = idToken
                provider = Google
                this.nonce = nonce
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error signing in with Google", e)
            Result.Error(AuthError.UnknownError)
        }
    }

    override suspend fun signUp(payload: SignUpDTO): EmptyResult<AuthError> {
        return try {
            auth.signUpWith(Email) {
                email = payload.email
                password = payload.password
            }
            Result.Success(Unit)
        } catch (e: RestException) {
            when (e.statusCode) {
                400 -> Result.Error(AuthError.InvalidEmail)
                422 -> Result.Error(AuthError.UserAlreadyExists)
                else -> Result.Error(AuthError.UnknownError)
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Unexpected error during sign-up: ${e.message}", e)
            Result.Error(AuthError.UnknownError)
        }
    }

    override suspend fun signOut(): EmptyResult<AuthError> {
        return try {
            auth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error signing out", e)
            Result.Error(AuthError.UnknownError)
        }
    }

}