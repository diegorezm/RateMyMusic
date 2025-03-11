package com.diegorezm.ratemymusic.modules.profiles.use_cases

import com.diegorezm.ratemymusic.modules.profiles.models.Profile
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

suspend fun createProfileUseCase(
    name: String,
    email: String,
    photoUrl: String?
): Result<Unit> {
    val db = Firebase.firestore
    return try {
        val profile = Profile(name, email, photoUrl)
        db.collection("profiles").add(profile).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}