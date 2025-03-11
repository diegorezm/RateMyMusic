package com.diegorezm.ratemymusic.modules.profiles.use_cases

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

suspend fun checkIfProfileExistsUseCase(email: String): Boolean {
    val db = Firebase.firestore
    return try {
        val querySnapshot = db.collection("profiles")
            .whereEqualTo("email", email)
            .get()
            .await()
        !querySnapshot.isEmpty
    } catch (e: Exception) {
        Log.e("AuthViewModel", "Error checking profile: ${e.message}", e)
        false
    }
}