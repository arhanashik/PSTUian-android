package com.workfort.pstuian.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.ceil
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface DateTimeUtil {
    fun getTimeInMillisNow(): Long

    /**
     * Returns time in milliseconds for today at midnight 00:00:00
     * */
    fun getTimeInMillsAtMidnight(): Long

    /**
     * This function gets a date long and format it to the target format.
     * Returns string
     * */
    fun getTimeInMillsUntilMidnight(): Long

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
    fun getTimeAgo(dateStr: String): String

    /**
     * This function gets a date string and compare it with current date and time.
     * Returns positive integer for days remaining in the future.
     * Returns negative integer for days are gone in the past
     *
     * Input example: "2020-07-28T00:00:00.000Z"
     * Sample input "2016-01-24 16:00:00" (current)
     * */
    fun getDaysMore(inputDateString: String): Int

    fun getRemainingHoursToNextDay(timestamp: Long): Int

    fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean

    fun formatDateDDMMMYYYYHHSS(timestamp: Long): String

    fun formatDateDDMMMYYYY(timestamp: Long): String

    fun formatDateYYYYMMDD(timestamp: Long): String

    fun formatTimeHHSS(timestamp: Long): String
}

@OptIn(ExperimentalTime::class)
class DateTimeUtilImpl : DateTimeUtil {

    override fun getTimeInMillisNow(): Long = Clock.System.now().toEpochMilliseconds()

    override fun getTimeInMillsAtMidnight(): Long {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val midnight = LocalDateTime(now.year, now.month, now.day, 0, 0, 0)
        return midnight.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    override fun getTimeInMillsUntilMidnight(): Long {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val midnight = LocalDateTime(now.year, now.month, now.day, 23, 59, 59)
        return midnight.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    override fun getTimeAgo(dateStr: String): String {
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
                    "${dateTime.day} ${dateTime.month.name} ${dateTime.year}"
                }
            }
        } catch (_: Exception) {
            dateStr
        }
    }

    override fun getDaysMore(inputDateString: String): Int {
        return try {
            val isoDateStr = if (inputDateString.contains("T")) inputDateString else inputDateString.replace(" ", "T")
            val inputInstant = Instant.parse(isoDateStr)
            val now = Clock.System.now()
            (inputInstant - now).inWholeDays.toInt()
        } catch (_: Exception) {
            0
        }
    }

    override fun getRemainingHoursToNextDay(timestamp: Long): Int {
        val now = getTimeInMillisNow()
        val diffMillis = now - timestamp
        val twentyFourHoursMillis = 24 * 60 * 60 * 1000L

        return if (diffMillis < twentyFourHoursMillis) {
            val remainingMillis = twentyFourHoursMillis - diffMillis
            ceil(remainingMillis.toDouble() / (60 * 60 * 1000)).toInt()
        } else {
            0
        }
    }

    override fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        val instant1 = Instant.fromEpochMilliseconds(timestamp1)
        val instant2 = Instant.fromEpochMilliseconds(timestamp2)

        val dateTime1 = instant1.toLocalDateTime(TimeZone.currentSystemDefault())
        val dateTime2 = instant2.toLocalDateTime(TimeZone.currentSystemDefault())

        return dateTime1.date == dateTime2.date
    }

    override fun formatDateDDMMMYYYYHHSS(timestamp: Long): String {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val monthStr = dateTime.month.name.take(3).lowercase()
            .replaceFirstChar { it.uppercase() }
        val hour = dateTime.hour.toString().padStart(2, '0')
        val minute = dateTime.minute.toString().padStart(2, '0')
        return "${dateTime.day} $monthStr ${dateTime.year}, $hour:$minute"
    }

    override fun formatDateDDMMMYYYY(timestamp: Long): String {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val monthStr = dateTime.month.name.take(3).lowercase()
            .replaceFirstChar { it.uppercase() }
        return "${dateTime.day} $monthStr ${dateTime.year}"
    }

    override fun formatDateYYYYMMDD(timestamp: Long): String {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val year = dateTime.year
        val month = dateTime.month.number.toString().padStart(2, '0')
        val day = dateTime.day.toString().padStart(2, '0')
        return "$year-$month-$day"
    }

    override fun formatTimeHHSS(timestamp: Long): String {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val hour = dateTime.hour.toString().padStart(2, '0')
        val minute = dateTime.minute.toString().padStart(2, '0')
        return "$hour:$minute"
    }
}
