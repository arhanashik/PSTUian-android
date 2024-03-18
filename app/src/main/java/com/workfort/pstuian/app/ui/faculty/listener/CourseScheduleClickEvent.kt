package com.workfort.pstuian.app.ui.faculty.listener

import com.workfort.pstuian.model.CourseEntity

interface CourseScheduleClickEvent {
    fun onClickCourseSchedule(course: CourseEntity)
}