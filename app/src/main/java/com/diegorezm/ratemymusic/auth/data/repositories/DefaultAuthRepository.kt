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
import io.github.jan.supabase.postgrest.Postgrest


class DefaultAuthRepository(
    private val auth: Auth,
    private val db: Postgrest
) : AuthRepository {
    override suspend fun signIn(signInDTO: SignInDTO): EmptyResult<AuthError> {
        return try {
            auth.signInWith(Email) {
                email = signInDTO.email
                password = signInDTO.password
            }
            Result.Success(Unit)
        } catch (e: RestException) {
            Log.e("AuthRepository", "Error during sign-up: ${e.message}", e)
            return when (e.statusCode) {
                400 -> Result.Error(AuthError.InvalidCredentials)
                401 -> Result.Error(AuthError.InvalidCredentials)
                404 -> Result.Error(AuthError.UserNotFound)
                else -> Result.Error(AuthError.UnknownError)
            }
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
            Log.e("AuthRepository", "Error during sign-up: ${e.message}", e)
            return when (e.statusCode) {
                400 -> Result.Error(AuthError.InvalidCredentials)
                401 -> Result.Error(AuthError.InvalidCredentials)
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

    override suspend fun deleteAccount(): EmptyResult<AuthError> {
        return try {
            val currentUser = auth.currentUserOrNull()
            if (currentUser == null) {
                return Result.Error(AuthError.UserNotFound)
            }
            auth.admin.deleteUser(currentUser.id)
            db.from("profiles").delete {
                filter {
                    eq("uid", currentUser.id)
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error deleting account", e)
            Result.Error(AuthError.UnknownError)
        }
    }

}