package com.diegorezm.ratemymusic.auth.domain

import com.diegorezm.ratemymusic.auth.data.dto.SignInDTO
import com.diegorezm.ratemymusic.auth.data.dto.SignUpDTO
import com.diegorezm.ratemymusic.core.domain.EmptyResult

interface AuthRepository {
    suspend fun signIn(signInDTO: SignInDTO): EmptyResult<AuthError>
    suspend fun signInWithGoogle(idToken: String, rawNonce: String): EmptyResult<AuthError>

    suspend fun signUp(
        payload: SignUpDTO
    ): EmptyResult<AuthError>

    suspend fun signOut(): EmptyResult<AuthError>
    suspend fun deleteAccount(): EmptyResult<AuthError>
}