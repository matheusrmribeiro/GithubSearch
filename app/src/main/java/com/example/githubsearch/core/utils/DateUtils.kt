package com.example.githubsearch.core.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun getDate(): String {
        val currentTime: Date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(currentTime)
    }

    fun generateId() = (System.currentTimeMillis() / 1000).toString()
}