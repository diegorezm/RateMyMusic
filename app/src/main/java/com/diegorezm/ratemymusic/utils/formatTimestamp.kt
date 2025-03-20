package com.diegorezm.ratemymusic.utils

import android.icu.text.SimpleDateFormat
import android.util.Log
import com.google.firebase.Timestamp
import java.util.Locale

fun formatTimestamp(dateString: String): String {
    return try {
        val originalFormat =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val targetFormat = SimpleDateFormat(
            "MMMM dd, yyyy",
            Locale.getDefault()
        )
        val date = originalFormat.parse(dateString)
        date?.let { targetFormat.format(it) }
            ?: dateString
    } catch (e: Exception) {
        Log.e("TimestampFormatter", "Error formatting date: $dateString", e)
        dateString
    }
}

fun formatFirebaseTimestamp(dateString: Timestamp): String {
    return try {
        val date = dateString.toDate()
        val targetFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        targetFormat.format(date)
    } catch (e: Exception) {
        Log.e("TimestampFormatter", "Error formatting date: $dateString", e)
        dateString.toString()
    }
}