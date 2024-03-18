package com.workfort.pstuian.util.di

import com.workfort.pstuian.database.service.BatchService
import com.workfort.pstuian.database.service.ConfigService
import com.workfort.pstuian.database.service.CourseService
import com.workfort.pstuian.database.service.EmployeeService
import com.workfort.pstuian.database.service.FacultyService
import com.workfort.pstuian.database.service.SliderService
import com.workfort.pstuian.database.service.StudentService
import com.workfort.pstuian.database.service.TeacherService
import com.workfort.pstuian.networking.impl.BloodDonationRequestApiHelperImpl
import com.workfort.pstuian.networking.impl.CheckInApiHelperImpl
import com.workfort.pstuian.networking.impl.CheckInLocationApiHelperImpl
import com.workfort.pstuian.networking.impl.DonationApiHelperImpl
import com.workfort.pstuian.networking.impl.FacultyApiHelperImpl
import com.workfort.pstuian.networking.impl.NotificationApiHelperImpl
import com.workfort.pstuian.networking.impl.StudentApiHelperImpl
import com.workfort.pstuian.networking.impl.SupportApiHelperImpl
import com.workfort.pstuian.networking.impl.TeacherApiHelperImpl
import com.workfort.pstuian.app.data.database.AppDatabase
import com.workfort.pstuian.app.data.repository.AuthRepositoryImpl
import com.workfort.pstuian.repository.AuthRepository
import com.workfort.pstuian.repository.BloodDonationRepository
import com.workfort.pstuian.repository.BloodDonationRequestRepository
import com.workfort.pstuian.repository.CheckInLocationRepository
import com.workfort.pstuian.repository.CheckInRepository
import com.workfort.pstuian.repository.DonationRepository
import com.workfort.pstuian.repository.FacultyRepository
import com.workfort.pstuian.repository.NotificationRepository
import com.workfort.pstuian.repository.SliderRepository
import com.workfort.pstuian.repository.StudentRepository
import com.workfort.pstuian.repository.SupportRepository
import com.workfort.pstuian.repository.TeacherRepository
import org.koin.dsl.module

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
    single<com.workfort.pstuian.networking.AuthApiHelper> {
        com.workfort.pstuian.networking.impl.AuthApiHelperImpl(
            get()
        )
    }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    // slider repository injections
    single { com.workfort.pstuian.networking.SliderApiHelper(get()) }
    single { SliderRepository(get(), get()) }

    // faculty repository injections
    single<com.workfort.pstuian.networking.FacultyApiHelper> { FacultyApiHelperImpl(get()) }
    single { FacultyRepository(get(), get(), get(), get(), get(), get(), get()) }

    // student repository injections
    single<com.workfort.pstuian.networking.StudentApiHelper> { StudentApiHelperImpl(get()) }
    single { StudentRepository(get(), get(), get(), get()) }

    // teacher repository injections
    single<com.workfort.pstuian.networking.TeacherApiHelper> { TeacherApiHelperImpl(get()) }
    single { TeacherRepository(get(), get(), get(), get()) }

    // donation repository injections
    single<com.workfort.pstuian.networking.DonationApiHelper> { DonationApiHelperImpl(get()) }
    single { DonationRepository(get()) }

    // support repository injections
    single<com.workfort.pstuian.networking.SupportApiHelper> { SupportApiHelperImpl(get()) }
    single { SupportRepository(get()) }

    // notification repository injections
    single<com.workfort.pstuian.networking.NotificationApiHelper> { NotificationApiHelperImpl(get()) }
    single { NotificationRepository(get(), get()) }

    // blood donation repository injections
    single<com.workfort.pstuian.networking.BloodDonationApiHelper> {
        com.workfort.pstuian.networking.impl.BloodDonationApiHelperImpl(
            get()
        )
    }
    single { BloodDonationRepository(get(), get()) }

    // blood donation request repository injections
    single<com.workfort.pstuian.networking.BloodDonationRequestApiHelper> { BloodDonationRequestApiHelperImpl(get()) }
    single { BloodDonationRequestRepository(get(), get()) }

    // check in repository injections
    single<com.workfort.pstuian.networking.CheckInApiHelper> { CheckInApiHelperImpl(get()) }
    single { CheckInRepository(get(), get()) }

    // check in location repository injections
    single<com.workfort.pstuian.networking.CheckInLocationApiHelper> { CheckInLocationApiHelperImpl(get()) }
    single { CheckInLocationRepository(get(), get()) }
}