package com.workfort.pstuian.app.data.remote

import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.app.data.local.course.CourseEntity

data class CourseScheduleResponse (val success: Boolean,
                                   val message: String,
                                   @SerializedName("course_schedules")
                                   val courses: ArrayList<CourseEntity>)