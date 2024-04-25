package com.workfort.pstuian.util.di

import com.workfort.pstuian.app.data.database.AppDatabase
import com.workfort.pstuian.database.service.BatchDbService
import com.workfort.pstuian.database.service.ConfigDbService
import com.workfort.pstuian.database.service.CourseDbService
import com.workfort.pstuian.database.service.EmployeeDbService
import com.workfort.pstuian.database.service.FacultyDbService
import com.workfort.pstuian.database.service.SliderDbService
import com.workfort.pstuian.database.service.StudentDbService
import com.workfort.pstuian.database.service.TeacherDbService
import org.koin.dsl.module


private val db = AppDatabase.getDatabase()
val databaseModule = module {
    // db services injection
    single { ConfigDbService(db.configDao()) }
    single { SliderDbService(db.sliderDao()) }
    single { FacultyDbService(db.facultyDao()) }
    single { BatchDbService(db.batchDao()) }
    single { StudentDbService(db.studentDao()) }
    single { TeacherDbService(db.teacherDao()) }
    single { CourseDbService(db.courseScheduleDao()) }
    single { EmployeeDbService(db.employeeDao()) }
}