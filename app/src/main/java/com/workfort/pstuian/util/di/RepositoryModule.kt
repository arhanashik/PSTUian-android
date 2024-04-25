package com.workfort.pstuian.util.di

import com.workfort.pstuian.app.data.repository.AuthRepositoryImpl
import com.workfort.pstuian.app.data.repository.SettingsRepository
import com.workfort.pstuian.networking.domain.AuthApiHelper
import com.workfort.pstuian.networking.domain.BloodDonationApiHelper
import com.workfort.pstuian.networking.domain.BloodDonationRequestApiHelper
import com.workfort.pstuian.networking.domain.CheckInApiHelper
import com.workfort.pstuian.networking.domain.CheckInLocationApiHelper
import com.workfort.pstuian.networking.domain.DonationApiHelper
import com.workfort.pstuian.networking.domain.FacultyApiHelper
import com.workfort.pstuian.networking.domain.NotificationApiHelper
import com.workfort.pstuian.networking.domain.SliderApiHelper
import com.workfort.pstuian.networking.domain.StudentApiHelper
import com.workfort.pstuian.networking.domain.SupportApiHelper
import com.workfort.pstuian.networking.domain.TeacherApiHelper
import com.workfort.pstuian.networking.infrastructure.AuthApiHelperImpl
import com.workfort.pstuian.networking.infrastructure.BloodDonationApiHelperImpl
import com.workfort.pstuian.networking.infrastructure.BloodDonationRequestApiHelperImpl
import com.workfort.pstuian.networking.infrastructure.CheckInApiHelperImpl
import com.workfort.pstuian.networking.infrastructure.CheckInLocationApiHelperImpl
import com.workfort.pstuian.networking.infrastructure.DonationApiHelperImpl
import com.workfort.pstuian.networking.infrastructure.FacultyApiHelperImpl
import com.workfort.pstuian.networking.infrastructure.NotificationApiHelperImpl
import com.workfort.pstuian.networking.infrastructure.StudentApiHelperImpl
import com.workfort.pstuian.networking.infrastructure.SupportApiHelperImpl
import com.workfort.pstuian.networking.infrastructure.TeacherApiHelperImpl
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


val repositoryModule = module {
    // auth repository injections
    single<AuthApiHelper> { AuthApiHelperImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

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

    // settings repository
    single { SettingsRepository() }
}