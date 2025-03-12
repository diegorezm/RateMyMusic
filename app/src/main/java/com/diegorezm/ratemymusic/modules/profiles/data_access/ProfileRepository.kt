package com.diegorezm.ratemymusic.modules.profiles.data_access

import android.util.Log
import com.diegorezm.ratemymusic.modules.profiles.models.Profile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val tag = "ProfileRepository"

    suspend fun create(
        uid: String,
        name: String,
        email: String,
        photoUrl: String? = null
    ): Result<Unit> {
        Log.i(tag, "Creating profile for user with UID: $uid")
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
    
    suspend fun checkIfProfileExists(uid: String): Boolean {
        Log.i(tag, "Checking if profile exists for user with UID: $uid")
        return try {
            val querySnapshot = db.collection("profiles")
                .document(uid)
                .get()
                .await()
            querySnapshot.exists()
        } catch (e: Exception) {
            Log.e("CheckIfProfileExistsError", "Error checking profile: ${e.message}", e)
            false
        }
    }

    suspend fun getByUserId(uid: String): Result<Profile?> {
        Log.i(tag, "Getting profile for user with UID: $uid")
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
}