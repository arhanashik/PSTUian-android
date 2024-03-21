package com.workfort.pstuian.app.data.database

import com.workfort.pstuian.database.service.BatchService
import com.workfort.pstuian.database.service.CourseService
import com.workfort.pstuian.database.service.EmployeeService
import com.workfort.pstuian.database.service.FacultyService
import com.workfort.pstuian.database.service.SliderService
import com.workfort.pstuian.database.service.StudentService
import com.workfort.pstuian.database.service.TeacherService

object DatabaseHelper {
    private val db = AppDatabase.getDatabase()
    fun provideSliderService() = SliderService(db.sliderDao())
    fun provideFacultyService() = FacultyService(db.facultyDao())
    fun provideStudentService() = StudentService(db.studentDao())
    fun provideTeacherService() = TeacherService(db.teacherDao())
    fun provideBatchService() = BatchService(db.batchDao())
    fun provideEmployeeService() = EmployeeService(db.employeeDao())
    fun provideCourseScheduleService() = CourseService(db.courseScheduleDao())
}