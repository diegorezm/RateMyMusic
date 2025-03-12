package com.diegorezm.ratemymusic.modules.profiles.use_cases

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

suspend fun checkIfProfileExistsUseCase(
    uid: String,
    db: FirebaseFirestore = Firebase.firestore
): Boolean {
    return try {
        Log.i("CheckIfProfileExists", "Checking if profile exists for user with UID: $uid")
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