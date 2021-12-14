package com.workfort.pstuian.util.lib.koin

import com.workfort.pstuian.app.data.local.batch.BatchService
import com.workfort.pstuian.app.data.local.config.ConfigService
import com.workfort.pstuian.app.data.local.course.CourseService
import com.workfort.pstuian.app.data.local.database.AppDatabase
import com.workfort.pstuian.app.data.local.employee.EmployeeService
import com.workfort.pstuian.app.data.local.faculty.FacultyService
import com.workfort.pstuian.app.data.local.slider.SliderService
import com.workfort.pstuian.app.data.local.student.StudentService
import com.workfort.pstuian.app.data.local.teacher.TeacherService
import com.workfort.pstuian.app.data.remote.apihelper.*
import com.workfort.pstuian.app.data.repository.*
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
    // db services injection
    single { ConfigService(db.configDao()) }
    single { SliderService(db.sliderDao()) }
    single { FacultyService(db.facultyDao()) }
    single { BatchService(db.batchDao()) }
    single { StudentService(db.studentDao()) }
    single { TeacherService(db.teacherDao()) }
    single { CourseService(db.courseScheduleDao()) }
    single { EmployeeService(db.employeeDao()) }

    // auth repository injections
    single<AuthApiHelper> { AuthApiHelperImpl(get())}
    single { AuthRepository(get(), get()) }

    // slider repository injections
    single { SliderApiHelper(get()) }
    single { SliderRepository(get(), get()) }

    // faculty repository injections
    single<FacultyApiHelper> { FacultyApiHelperImpl(get()) }
    single { FacultyRepository(get(), get(), get(), get(), get(), get(), get()) }

    // student repository injections
    single<StudentApiHelper> { StudentApiHelperImpl(get()) }
    single { StudentRepository(get(), get(), get(), get()) }

    // teacher repository injections
    single<TeacherApiHelper> { TeacherApiHelperImpl(get()) }
    single { TeacherRepository(get(), get(), get(), get()) }

    // donation repository injections
    single<DonationApiHelper> { DonationApiHelperImpl(get()) }
    single { DonationRepository(get()) }

    // support repository injections
    single<SupportApiHelper> { SupportApiHelperImpl(get()) }
    single { SupportRepository(get()) }

    // notification repository injections
    single<NotificationApiHelper> { NotificationApiHelperImpl(get()) }
    single { NotificationRepository(get(), get()) }

    // blood donation repository injections
    single<BloodDonationApiHelper> { BloodDonationApiHelperImpl(get()) }
    single { BloodDonationRepository(get(), get()) }

    // blood donation request repository injections
    single<BloodDonationRequestApiHelper> { BloodDonationRequestApiHelperImpl(get()) }
    single { BloodDonationRequestRepository(get(), get()) }

    // check in repository injections
    single<CheckInApiHelper> { CheckInApiHelperImpl(get()) }
    single { CheckInRepository(get(), get()) }

    // check in location repository injections
    single<CheckInLocationApiHelper> { CheckInLocationApiHelperImpl(get()) }
    single { CheckInLocationRepository(get(), get()) }
}