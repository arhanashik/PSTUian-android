package com.workfort.pstuian.database.di

import com.workfort.pstuian.database.AppDatabase
import com.workfort.pstuian.database.getRoomDatabase
import com.workfort.pstuian.database.service.BatchDbService
import com.workfort.pstuian.database.service.ConfigDbService
import com.workfort.pstuian.database.service.CourseDbService
import com.workfort.pstuian.database.service.EmployeeDbService
import com.workfort.pstuian.database.service.FacultyDbService
import com.workfort.pstuian.database.service.SliderDbService
import com.workfort.pstuian.database.service.StudentDbService
import com.workfort.pstuian.database.service.TeacherDbService
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> { getRoomDatabase(get()) }

    // db services injection
    single { ConfigDbService(get<AppDatabase>().configDao()) }
    single { SliderDbService(get<AppDatabase>().sliderDao()) }
    single { FacultyDbService(get<AppDatabase>().facultyDao()) }
    single { BatchDbService(get<AppDatabase>().batchDao()) }
    single { StudentDbService(get<AppDatabase>().studentDao()) }
    single { TeacherDbService(get<AppDatabase>().teacherDao()) }
    single { CourseDbService(get<AppDatabase>().courseDao()) }
    single { EmployeeDbService(get<AppDatabase>().employeeDao()) }
}
