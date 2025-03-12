package com.diegorezm.ratemymusic.modules.profiles.use_cases

import android.util.Log
import com.diegorezm.ratemymusic.modules.profiles.models.Profile
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

suspend fun createProfileUseCase(
    uid: String,
    name: String,
    email: String,
    photoUrl: String?,
    db: FirebaseFirestore = Firebase.firestore
): Result<Unit> {
    Log.i("CreateProfileUseCase", "Creating profile for user with UID: $uid")
    return try {
        val profile = Profile(name, email, photoUrl)
        db.collection("profiles")
            .document(uid)
            .set(profile)
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("CreateProfileError", "Error creating profile: ${e.message}", e)
        Result.failure(e)
    }
}