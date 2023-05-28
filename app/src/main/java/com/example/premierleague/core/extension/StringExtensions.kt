package com.example.premierleague.core.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// 2022-08-05T19:00:00Z
fun String.getFormattedDate(): String {
    if (this.isBlank()) return ""
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("en"))
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val date = sdf.parse(this)
    val formatter = SimpleDateFormat("EEE d MMM")
    return formatter.format(date)
}

//fun String?.getDate(): Date? {
//    if (this == null)
//        return null
//    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("en"))
//    sdf.timeZone = TimeZone.getTimeZone("UTC")
//    return sdf.parse(this)
//}
fun String?.getDate(): Date? {
    if (this == null)
        return null
    return try {
        val sdf = SimpleDateFormat("EEE d MMM", Locale("en"))
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        sdf.parse(this)
    } catch (e: Exception) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("en"))
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        sdf.parse(this)
    }
}

fun Date?.getDateString(): String {
    if (this == null) return ""
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("en"))
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val date: String = sdf.format(this)
    return date
}