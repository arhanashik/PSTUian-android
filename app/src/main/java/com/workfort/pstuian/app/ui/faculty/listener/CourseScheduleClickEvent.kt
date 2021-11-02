package com.workfort.pstuian.app.ui.faculty.listener

import com.workfort.pstuian.app.data.local.course.CourseEntity

interface CourseScheduleClickEvent {
    fun onClickCourseSchedule(course: CourseEntity)
}