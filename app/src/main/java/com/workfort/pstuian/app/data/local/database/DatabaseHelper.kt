package com.workfort.pstuian.app.data.local.database

import com.workfort.pstuian.app.data.local.batch.BatchService
import com.workfort.pstuian.app.data.local.course.CourseService
import com.workfort.pstuian.app.data.local.employee.EmployeeService
import com.workfort.pstuian.app.data.local.faculty.FacultyService
import com.workfort.pstuian.app.data.local.slider.SliderService
import com.workfort.pstuian.app.data.local.student.StudentService
import com.workfort.pstuian.app.data.local.teacher.TeacherService

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