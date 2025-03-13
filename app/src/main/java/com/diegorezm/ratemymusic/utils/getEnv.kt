package com.diegorezm.ratemymusic.utils

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend fun getEnvRemote(
    name: String,
    db: FirebaseFirestore = FirebaseFirestore.getInstance()
): Result<String> {
    return try {
        val document = db.collection("env").document(name).get().await()
        val data = document.data
        val value = data?.get("value")
        if (value == null) {
            Log.e("Firebase", "Value is null for document $name")
            return Result.failure(Exception("Value is null"))
        }
        Result.success(value.toString())
    } catch (e: Exception) {
        Log.e("Firebase", "Error getting document", e)
        Result.failure(e)
    }

}

fun getEnvLocal(): String? {
    return null
}