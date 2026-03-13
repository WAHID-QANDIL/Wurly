package com.wahid.wurly.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object ExtUtils {
    fun Long.toDateAndTime(): String {
        val dateTime = Instant
            .ofEpochSecond(this)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }

}