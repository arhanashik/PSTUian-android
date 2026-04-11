package com.workfort.pstuian.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object DateUtil {
    /**
     * It's return date before one week timestamp
     *  like return
     *  1 day ago
     *  2 days ago
     *  5 days ago
     *  21 April 2019
     *  @param dateStr
     *  Sample input "2016-01-24T16:00:00.000Z" (old)
     *  Sample input "2016-01-24 16:00:00" (current)
     * */
    fun getTimeAgo(dateStr: String, format: String = "yyyy-MM-dd HH:mm:ss"): String {
        return try {
            val isoDateStr = if (dateStr.contains("T")) dateStr else dateStr.replace(" ", "T")
            val instant = Instant.parse(isoDateStr)
            val now = Clock.System.now()
            val duration = now - instant
            
            when {
                duration.inWholeMinutes < 1 -> "just now"
                duration.inWholeMinutes < 60 -> "${duration.inWholeMinutes} minutes ago"
                duration.inWholeHours < 24 -> "${duration.inWholeHours} hours ago"
                duration.inWholeDays < 7 -> "${duration.inWholeDays} days ago"
                else -> {
                    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                    "${dateTime.dayOfMonth} ${dateTime.month.name} ${dateTime.year}"
                }
            }
        } catch (e: Exception) {
            dateStr
        }
    }

    /**
     * This function gets a date string and compare it with current date and time.
     * Returns positive integer for days remaining in the future.
     * Returns negative integer for days are gone in the past
     *
     * Input example: "2020-07-28T00:00:00.000Z"
     * Sample input "2016-01-24 16:00:00" (current)
     * */
    fun getDaysMore(inputDateString: String, format: String = "yyyy-MM-dd HH:mm:ss"): Int {
        return try {
            val isoDateStr = if (inputDateString.contains("T")) inputDateString else inputDateString.replace(" ", "T")
            val inputInstant = Instant.parse(isoDateStr)
            val now = Clock.System.now()
            (inputInstant - now).inWholeDays.toInt()
        } catch (e: Exception) {
            0
        }
    }

    /**
     * This function gets a date long and format it to the target format.
     * Returns string
     * */
    fun format(inputDate: Long, format: String = "yyyy-MM-dd HH:mm:ss"): String {
        val instant = Instant.fromEpochMilliseconds(inputDate)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${dateTime.year}-${dateTime.monthNumber.toString().padStart(2, '0')}-${dateTime.dayOfMonth.toString().padStart(2, '0')} " +
                "${dateTime.hour.toString().padStart(2, '0')}:${dateTime.minute.toString().padStart(2, '0')}:${dateTime.second.toString().padStart(2, '0')}"
    }

    /**
     * Returns time in milliseconds for today until midnight 23:59:59
     * */
    fun getTimeInMillsUntilMidnight(): Long {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val midnight = LocalDateTime(now.year, now.month, now.dayOfMonth, 23, 59, 59)
        return midnight.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    /**
     * Returns time in milliseconds for today at midnight 00:00:00
     * */
    fun getTimeInMillsAtMidnight(): Long {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val midnight = LocalDateTime(now.year, now.month, now.dayOfMonth, 0, 0, 0)
        return midnight.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }
}
