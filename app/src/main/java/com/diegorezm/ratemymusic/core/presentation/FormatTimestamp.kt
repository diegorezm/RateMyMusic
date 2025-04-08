package com.diegorezm.ratemymusic.core.presentation

import android.util.Log
import java.text.SimpleDateFormat
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