package com.workfort.pstuian.app.ui.home.faculty.listener

import com.workfort.pstuian.app.data.local.courseschedule.CourseScheduleEntity

interface CourseScheduleClickEvent {
    fun onClickCourseSchedule(courseSchedule: CourseScheduleEntity)
}