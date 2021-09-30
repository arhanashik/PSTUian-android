package com.workfort.pstuian.app.data.local.database

import com.workfort.pstuian.app.data.local.batch.BatchService
import com.workfort.pstuian.app.data.local.courseschedule.CourseScheduleService
import com.workfort.pstuian.app.data.local.employee.EmployeeService
import com.workfort.pstuian.app.data.local.faculty.FacultyService
import com.workfort.pstuian.app.data.local.slider.SliderService
import com.workfort.pstuian.app.data.local.student.StudentService
import com.workfort.pstuian.app.data.local.teacher.TeacherService

class DatabaseHelper {
    companion object {

        fun provideSliderService(): SliderService {
            return SliderService(AppDatabase.getDatabase()!!.sliderDao())
        }

        fun provideFacultyService(): FacultyService {
            return FacultyService(AppDatabase.getDatabase()!!.facultyDao())
        }

        fun provideStudentService(): StudentService {
            return StudentService(AppDatabase.getDatabase()!!.studentDao())
        }

        fun provideTeacherService(): TeacherService {
            return TeacherService(AppDatabase.getDatabase()!!.teacherDao())
        }

        fun provideBatchService(): BatchService {
            return BatchService(AppDatabase.getDatabase()!!.batchDao())
        }

        fun provideEmployeeService(): EmployeeService {
            return EmployeeService(AppDatabase.getDatabase()!!.employeeDao())
        }

        fun provideCourseScheduleService(): CourseScheduleService {
            return CourseScheduleService(AppDatabase.getDatabase()!!.courseScheduleDao())
        }
    }
}