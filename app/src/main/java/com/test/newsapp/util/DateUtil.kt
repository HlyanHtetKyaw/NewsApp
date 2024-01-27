package com.test.newsapp.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale

object DateUtil {

    fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
            val mDate = inputFormat.parse(dateString)
            val outputFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH)
            return outputFormat.format(mDate!!)
        } catch (e: Exception) {
            Log.e("mDate", e.toString())
            e.toString()
        }
    }
}