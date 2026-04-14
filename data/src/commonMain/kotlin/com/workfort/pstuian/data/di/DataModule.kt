package com.workfort.pstuian.data.di

import com.workfort.pstuian.data.infrastructure.repository.BloodDonationRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.BloodDonationRequestRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.CheckInLocationRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.CheckInRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.DonationRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.FacultyRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.NotificationRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.SettingsRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.SharedPrefRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.SliderRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.StudentRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.SupportRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.TeacherRepositoryImpl
import com.workfort.pstuian.data.local.database.AppDatabase
import com.workfort.pstuian.data.local.database.getRoomDatabase
import com.workfort.pstuian.data.local.database.service.BatchDbService
import com.workfort.pstuian.data.local.database.service.ConfigDbService
import com.workfort.pstuian.data.local.database.service.CourseDbService
import com.workfort.pstuian.data.local.database.service.EmployeeDbService
import com.workfort.pstuian.data.local.database.service.FacultyDbService
import com.workfort.pstuian.data.local.database.service.SliderDbService
import com.workfort.pstuian.data.local.database.service.StudentDbService
import com.workfort.pstuian.data.local.database.service.TeacherDbService
import com.workfort.pstuian.data.local.keyvaluestorage.Prefs
import com.workfort.pstuian.data.remote.KtorClientFactory
import com.workfort.pstuian.data.remote.domain.AuthApiHelper
import com.workfort.pstuian.data.remote.domain.BloodDonationApiHelper
import com.workfort.pstuian.data.remote.domain.BloodDonationRequestApiHelper
import com.workfort.pstuian.data.remote.domain.CheckInApiHelper
import com.workfort.pstuian.data.remote.domain.CheckInLocationApiHelper
import com.workfort.pstuian.data.remote.domain.DonationApiHelper
import com.workfort.pstuian.data.remote.domain.FacultyApiHelper
import com.workfort.pstuian.data.remote.domain.NotificationApiHelper
import com.workfort.pstuian.data.remote.domain.SliderApiHelper
import com.workfort.pstuian.data.remote.domain.StudentApiHelper
import com.workfort.pstuian.data.remote.domain.SupportApiHelper
import com.workfort.pstuian.data.remote.domain.TeacherApiHelper
import com.workfort.pstuian.data.remote.infrastructure.AuthApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.BloodDonationApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.BloodDonationRequestApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.CheckInApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.CheckInLocationApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.DonationApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.FacultyApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.NotificationApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.StudentApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.SupportApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.TeacherApiHelperImpl
import com.workfort.pstuian.data.remote.service.AuthApiService
import com.workfort.pstuian.data.remote.service.BloodDonationApiService
import com.workfort.pstuian.data.remote.service.BloodDonationRequestApiService
import com.workfort.pstuian.data.remote.service.CheckInApiService
import com.workfort.pstuian.data.remote.service.CheckInLocationApiService
import com.workfort.pstuian.data.remote.service.DonationApiService
import com.workfort.pstuian.data.remote.service.FacultyApiService
import com.workfort.pstuian.data.remote.service.FileHandlerApiService
import com.workfort.pstuian.data.remote.service.NotificationApiService
import com.workfort.pstuian.data.remote.service.SliderApiService
import com.workfort.pstuian.data.remote.service.StudentApiService
import com.workfort.pstuian.data.remote.service.SupportApiService
import com.workfort.pstuian.data.remote.service.TeacherApiService
import com.workfort.pstuian.featuredomain.model.SharedPrefKey
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.BloodDonationRepository
import com.workfort.pstuian.featuredomain.repository.BloodDonationRequestRepository
import com.workfort.pstuian.featuredomain.repository.CheckInLocationRepository
import com.workfort.pstuian.featuredomain.repository.CheckInRepository
import com.workfort.pstuian.featuredomain.repository.DonationRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.NotificationRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository
import com.workfort.pstuian.featuredomain.repository.SliderRepository
import com.workfort.pstuian.featuredomain.repository.StudentRepository
import com.workfort.pstuian.featuredomain.repository.SupportRepository
import com.workfort.pstuian.featuredomain.repository.TeacherRepository
import com.workfort.pstuian.data.infrastructure.repository.AuthRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformDataModule: Module

private val databaseModule = module {
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

private val networkModule = module {
    single {
        KtorClientFactory.create(
            authTokenProvider = {
                get<SharedPrefRepository>().getString(SharedPrefKey.AUTH_TOKEN)
            },
        )
    }
    single { AuthApiService(get()) }
    single { SliderApiService(get()) }
    single { FacultyApiService(get()) }
    single { StudentApiService(get()) }
    single { TeacherApiService(get()) }
    single { DonationApiService(get()) }
    single { FileHandlerApiService(get()) }
    single { SupportApiService(get()) }
    single { NotificationApiService(get()) }
    single { BloodDonationApiService(get()) }
    single { BloodDonationRequestApiService(get()) }
    single { CheckInApiService(get()) }
    single { CheckInLocationApiService(get()) }
}

private val sharedPrefModule = module {
    single { Prefs(get()) }
}

val repositoryModule = module {
    // auth repository injections
    factoryOf(::AuthApiHelperImpl) bind AuthApiHelper::class
    factoryOf(::AuthRepositoryImpl) bind AuthRepository::class

    // slider repository injections
    factoryOf(::SliderApiHelper)
    factoryOf(::SliderRepositoryImpl) bind SliderRepository::class

    // faculty repository injections
    factoryOf(::FacultyApiHelperImpl) bind FacultyApiHelper::class
    factoryOf(::FacultyRepositoryImpl) bind FacultyRepository::class

    // student repository injections
    factoryOf(::StudentApiHelperImpl) bind StudentApiHelper::class
    factoryOf(::StudentRepositoryImpl) bind StudentRepository::class

    // teacher repository injections
    factoryOf(::TeacherApiHelperImpl) bind TeacherApiHelper::class
    factoryOf(::TeacherRepositoryImpl) bind TeacherRepository::class

    // donation repository injections
    factoryOf(::DonationApiHelperImpl) bind DonationApiHelper::class
    factoryOf(::DonationRepositoryImpl) bind DonationRepository::class

    // support repository injections
    factoryOf(::SupportApiHelperImpl) bind SupportApiHelper::class
    factoryOf(::SupportRepositoryImpl) bind SupportRepository::class

    // notification repository injections
    factoryOf(::NotificationApiHelperImpl) bind NotificationApiHelper::class
    factoryOf(::NotificationRepositoryImpl) bind NotificationRepository::class

    // blood donation repository injections
    factoryOf(::BloodDonationApiHelperImpl) bind BloodDonationApiHelper::class
    factoryOf(::BloodDonationRepositoryImpl) bind BloodDonationRepository::class

    // blood donation request repository injections
    factoryOf(::BloodDonationRequestApiHelperImpl) bind BloodDonationRequestApiHelper::class
    factoryOf(::BloodDonationRequestRepositoryImpl) bind BloodDonationRequestRepository::class

    // check in repository injections
    factoryOf(::CheckInApiHelperImpl) bind CheckInApiHelper::class
    factoryOf(::CheckInRepositoryImpl) bind CheckInRepository::class

    // check in location repository injections
    factoryOf(::CheckInLocationApiHelperImpl) bind CheckInLocationApiHelper::class
    factoryOf(::CheckInLocationRepositoryImpl) bind CheckInLocationRepository::class

    // settings repository
    factoryOf(::SettingsRepositoryImpl) bind SettingsRepository::class

    factoryOf(::SharedPrefRepositoryImpl) bind SharedPrefRepository::class
}

val dataModule = listOf(
    platformDataModule,
    databaseModule,
    networkModule,
    sharedPrefModule,
    repositoryModule,
)
