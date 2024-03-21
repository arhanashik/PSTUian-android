package com.workfort.pstuian.util.helper

import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        // for 2 min ago   use  DateUtils.MINUTE_IN_MILLIS
        // for 2 sec ago   use  DateUtils.SECOND_IN_MILLIS
        // for 1 hours ago use  DateUtils.HOUR_IN_MILLIS
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        //sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
        return try {
            val date = sdf.parse(dateStr) ?: return dateStr
            val now = System.currentTimeMillis()
            DateUtils.getRelativeTimeSpanString(
                date.time, now, DateUtils.MINUTE_IN_MILLIS
            ).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
            dateStr
        } catch (e: Exception) {
            e.printStackTrace()
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
        val calCurr = Calendar.getInstance()
        val calInput = Calendar.getInstance()
        SimpleDateFormat(format, Locale.getDefault()).parse(inputDateString)?.also {
            calInput.time = it
        }

        return calInput.get(Calendar.DAY_OF_MONTH) - calCurr.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * This function gets a date long and format it to the target format.
     * Returns string
     * */
    fun format(inputDate: Long, format: String = "yyyy-MM-dd HH:mm:ss"): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(inputDate)
    }
}