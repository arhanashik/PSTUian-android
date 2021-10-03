package com.workfort.pstuian.util.lib.koin

import com.workfort.pstuian.app.data.local.batch.BatchService
import com.workfort.pstuian.app.data.local.course.CourseService
import com.workfort.pstuian.app.data.local.database.AppDatabase
import com.workfort.pstuian.app.data.local.employee.EmployeeService
import com.workfort.pstuian.app.data.local.faculty.FacultyService
import com.workfort.pstuian.app.data.local.slider.SliderService
import com.workfort.pstuian.app.data.local.student.StudentService
import com.workfort.pstuian.app.data.local.teacher.TeacherService
import com.workfort.pstuian.app.data.remote.apihelper.DonationApiHelperImpl
import com.workfort.pstuian.app.data.remote.apihelper.FacultyApiHelperImpl
import com.workfort.pstuian.app.data.remote.apihelper.SliderApiHelper
import com.workfort.pstuian.app.data.repository.DonationRepository
import com.workfort.pstuian.app.data.repository.FacultyRepository
import com.workfort.pstuian.app.data.repository.SliderRepository
import org.koin.dsl.module

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 4:58 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/1/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

private val db = AppDatabase.getDatabase()
val repositoryModule = module {
    //db services injection
    single { SliderService(db.sliderDao()) }
    single { FacultyService(db.facultyDao()) }
    single { BatchService(db.batchDao()) }
    single { StudentService(db.studentDao()) }
    single { TeacherService(db.teacherDao()) }
    single { CourseService(db.courseScheduleDao()) }
    single { EmployeeService(db.employeeDao()) }

    //slider repository injections
    single { SliderApiHelper(get()) }
    single { SliderRepository(get(), get()) }

    //faculty repository injections
    single { FacultyApiHelperImpl(get()) }
    single { FacultyRepository(get(), get(), get(), get(), get(), get(), get()) }

    //donation repository injections
    single { DonationApiHelperImpl(get()) }
    single { DonationRepository(get()) }
}