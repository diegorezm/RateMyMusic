package com.diegorezm.ratemymusic.modules.profiles.domain.repositories

import android.util.Log
import com.diegorezm.ratemymusic.modules.common.PublicException
import com.diegorezm.ratemymusic.modules.profiles.data.models.ProfileDTO
import com.diegorezm.ratemymusic.modules.profiles.data.models.toDomain
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.domain.models.Profile
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ProfileRepository {

    private val tag = "ProfileRepository"

    override suspend fun create(
        dto: ProfileDTO
    ): Result<Unit> {
        Log.i(tag, "Creating profile for user with UID: ${dto.uid}")
        return try {
            db.collection("profiles")
                .document(dto.uid)
                .set(dto.toDomain())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("CreateProfileError", "Error creating profile: ${e.message}", e)
            Result.failure(PublicException("Error creating profile."))
        }
    }

    override suspend fun checkIfProfileExists(uid: String): Boolean {
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

    override suspend fun getProfileById(uid: String): Result<Profile?> {
        return try {
            val querySnapshot = db.collection("profiles").document(uid)
                .get()
                .await()

            if (!querySnapshot.exists()) {
                return Result.failure(Exception("Profile not found"))
            }

            val profile = querySnapshot.toObject(Profile::class.java)
            Result.success(profile)
        } catch (e: Exception) {
            Log.e("GetProfileByIdError", "Error getting profile: ${e.message}", e)
            Result.failure(PublicException("Error getting profile."))
        }
    }

    override suspend fun getProfileByIds(uids: List<String>): Result<List<Profile>> {
        return try {
            val query = db.collection("profiles").whereIn(FieldPath.documentId(), uids)
            val querySnapshot = query.get().await()
            Result.success(querySnapshot.toObjects(Profile::class.java))
        } catch (e: Exception) {
            Log.e("GetProfileByIdsError", "Error getting profiles: ${e.message}", e)
            Result.failure(PublicException("Error getting profiles."))
        }
    }
}