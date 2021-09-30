package com.workfort.pstuian.app.ui.home.faculty.courseschedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.workfort.pstuian.app.data.local.courseschedule.CourseScheduleEntity
import com.workfort.pstuian.app.data.local.courseschedule.CourseScheduleService
import com.workfort.pstuian.app.data.local.database.DatabaseHelper

class CourseScheduleViewModel : ViewModel() {
    private var courseScheduleService: CourseScheduleService = DatabaseHelper.provideCourseScheduleService()
    private lateinit var courseScheduleLiveData: LiveData<List<CourseScheduleEntity>>

    fun getCourseSchedule(faculty: String): LiveData<List<CourseScheduleEntity>> {
        courseScheduleLiveData = courseScheduleService.getAllLive(faculty)

        return courseScheduleLiveData
    }

    fun insertCourseSchedule(courseSchedule: ArrayList<CourseScheduleEntity>) {
        Thread { courseScheduleService.insertAll(courseSchedule) }.start()
    }
}
