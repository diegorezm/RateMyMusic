package com.diegorezm.ratemymusic.modules.profiles.use_cases

import com.diegorezm.ratemymusic.modules.profiles.models.Profile
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

suspend fun getProfileUseCase(
    uid: String,
    db: FirebaseFirestore = Firebase.firestore
): Result<Profile> {
    return try {
        val querySnapshot = db.collection("profiles").document(uid)
            .get()
            .await()

        if (!querySnapshot.exists()) {
            return Result.failure(Exception("Profile not found"))
        }

        val data = querySnapshot.data

        if (data != null) {
            val profile = Profile(
                name = data["name"] as? String ?: "",
                email = data["email"] as? String ?: "",
                photoUrl = data["photoUrl"] as? String
            )
            Result.success(profile)
        } else {
            Result.failure(Exception("Failed to parse profile data"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}