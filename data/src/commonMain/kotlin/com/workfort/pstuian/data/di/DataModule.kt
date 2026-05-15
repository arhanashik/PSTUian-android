package com.workfort.pstuian.data.di

import com.workfort.pstuian.data.infrastructure.repository.AppConfigRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.AuthRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.BloodDonationRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.BloodDonationRequestRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.CheckInLocationRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.CheckInRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.CustomNotificationRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.DeviceRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.DonationRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.FacultyRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.FileHandlerRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.SettingsRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.SharedPrefRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.SliderRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.StudentRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.SupportRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.SystemNotificationRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.TeacherRepositoryImpl
import com.workfort.pstuian.data.infrastructure.repository.UserPresenceRepositoryImpl
import com.workfort.pstuian.data.remote.KtorClientFactory
import com.workfort.pstuian.data.remote.NetworkConst
import com.workfort.pstuian.data.remote.domain.AuthApiHelper
import com.workfort.pstuian.data.remote.domain.BloodDonationApiHelper
import com.workfort.pstuian.data.remote.domain.BloodDonationRequestApiHelper
import com.workfort.pstuian.data.remote.domain.CheckInApiHelper
import com.workfort.pstuian.data.remote.domain.CheckInLocationApiHelper
import com.workfort.pstuian.data.remote.domain.DeviceApiHelper
import com.workfort.pstuian.data.remote.domain.DonationApiHelper
import com.workfort.pstuian.data.remote.domain.FacultyApiHelper
import com.workfort.pstuian.data.remote.domain.NotificationApiHelper
import com.workfort.pstuian.data.remote.domain.SliderApiHelper
import com.workfort.pstuian.data.remote.domain.StudentApiHelper
import com.workfort.pstuian.data.remote.domain.SupportApiHelper
import com.workfort.pstuian.data.remote.domain.TeacherApiHelper
import com.workfort.pstuian.data.remote.firebase.FirebaseAuthDataSource
import com.workfort.pstuian.data.remote.firebase.FirebaseUserPresenceDataSource
import com.workfort.pstuian.data.remote.firestore.FirestoreAppConfigDataSource
import com.workfort.pstuian.data.remote.firestore.FirestoreSystemNotificationDataSource
import com.workfort.pstuian.data.remote.infrastructure.AuthApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.BloodDonationApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.BloodDonationRequestApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.CheckInApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.CheckInLocationApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.DeviceApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.DonationApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.FacultyApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.SliderApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.FileRemoteFetcherImpl
import com.workfort.pstuian.data.remote.infrastructure.NotificationApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.StudentApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.SupportApiHelperImpl
import com.workfort.pstuian.data.remote.infrastructure.TeacherApiHelperImpl
import com.workfort.pstuian.data.remote.service.AuthApiService
import com.workfort.pstuian.data.remote.service.BloodDonationApiService
import com.workfort.pstuian.data.remote.service.BloodDonationRequestApiService
import com.workfort.pstuian.data.remote.service.CheckInApiService
import com.workfort.pstuian.data.remote.service.CheckInLocationApiService
import com.workfort.pstuian.data.remote.service.CustomNotificationApiService
import com.workfort.pstuian.data.remote.service.DeviceApiService
import com.workfort.pstuian.data.remote.service.DonationApiService
import com.workfort.pstuian.data.remote.service.FacultyApiService
import com.workfort.pstuian.data.remote.service.FileHandlerApiService
import com.workfort.pstuian.data.remote.service.SliderApiService
import com.workfort.pstuian.data.remote.service.StudentApiService
import com.workfort.pstuian.data.remote.service.SupportApiService
import com.workfort.pstuian.data.remote.service.TeacherApiService
import com.workfort.pstuian.featuredomain.model.DebugApiEnvironment
import com.workfort.pstuian.featuredomain.model.SharedPrefKey
import com.workfort.pstuian.featuredomain.network.FileRemoteFetcher
import com.workfort.pstuian.featuredomain.repository.AppConfigRepository
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.BloodDonationRepository
import com.workfort.pstuian.featuredomain.repository.BloodDonationRequestRepository
import com.workfort.pstuian.featuredomain.repository.CheckInLocationRepository
import com.workfort.pstuian.featuredomain.repository.CheckInRepository
import com.workfort.pstuian.featuredomain.repository.CustomNotificationRepository
import com.workfort.pstuian.featuredomain.repository.DeviceRepository
import com.workfort.pstuian.featuredomain.repository.DonationRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.FileHandlerRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository
import com.workfort.pstuian.featuredomain.repository.SliderRepository
import com.workfort.pstuian.featuredomain.repository.StudentRepository
import com.workfort.pstuian.featuredomain.repository.SupportRepository
import com.workfort.pstuian.featuredomain.repository.SystemNotificationRepository
import com.workfort.pstuian.featuredomain.repository.TeacherRepository
import com.workfort.pstuian.featuredomain.repository.UserPresenceRepository
import com.workfort.pstuian.util.PlatformInfo
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.database
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformDataModule: Module

private val firebaseModule = module {
    // Firebase
    single { Firebase.auth }
    single { Firebase.firestore }
    single { Firebase.database }

    // Data Sources
    singleOf(::FirebaseAuthDataSource)
    singleOf(::FirestoreAppConfigDataSource)
    singleOf(::FirebaseUserPresenceDataSource)
    singleOf(::FirestoreSystemNotificationDataSource)
}

private val plainHttpClientQualifier = named("plainHttpClient")

private val networkModule = module {
    single(qualifier = plainHttpClientQualifier) {
        KtorClientFactory.createPlainHttpClient(platformInfo = get())
    }
    single<FileRemoteFetcher> {
        FileRemoteFetcherImpl(httpClient = get(qualifier = plainHttpClientQualifier))
    }

    single {
        KtorClientFactory.create(
            platformInfo = get(),
            baseUrlProvider = {
                val platformInfo = get<PlatformInfo>()
                val settingsRepository = get<SettingsRepository>()
                if (!platformInfo.isDebug) {
                    NetworkConst.Remote.PROD_API_SERVER
                } else {
                    when (settingsRepository.getDebugApiEnvironment()) {
                        DebugApiEnvironment.LOCAL -> NetworkConst.Remote.LOCAL_API_SERVER
                        DebugApiEnvironment.DEV -> NetworkConst.Remote.DEV_API_SERVER
                        DebugApiEnvironment.PROD -> NetworkConst.Remote.PROD_API_SERVER
                    }
                }
            },
            authTokenProvider = {
                get<SharedPrefRepository>().getString(SharedPrefKey.AUTH_TOKEN)
            },
        )
    }
    single { AuthApiService(get()) }
    factoryOf(::AuthApiHelperImpl) bind AuthApiHelper::class

    single { DeviceApiService(get()) }
    factoryOf(::DeviceApiHelperImpl) bind DeviceApiHelper::class

    single { SliderApiService(get()) }
    factoryOf(::SliderApiHelperImpl) bind SliderApiHelper::class

    single { FacultyApiService(get()) }
    factoryOf(::FacultyApiHelperImpl) bind FacultyApiHelper::class

    singleOf(::StudentApiService)
    singleOf(::StudentApiHelperImpl) bind StudentApiHelper::class

    single { TeacherApiService(get()) }
    factoryOf(::TeacherApiHelperImpl) bind TeacherApiHelper::class

    single { DonationApiService(get()) }
    factoryOf(::DonationApiHelperImpl) bind DonationApiHelper::class

    single { FileHandlerApiService(get()) }

    single { SupportApiService(get()) }
    factoryOf(::SupportApiHelperImpl) bind SupportApiHelper::class

    single { CustomNotificationApiService(get()) }
    factoryOf(::NotificationApiHelperImpl) bind NotificationApiHelper::class

    single { BloodDonationApiService(get()) }
    factoryOf(::BloodDonationApiHelperImpl) bind BloodDonationApiHelper::class

    single { BloodDonationRequestApiService(get()) }
    factoryOf(::BloodDonationRequestApiHelperImpl) bind BloodDonationRequestApiHelper::class

    single { CheckInApiService(get()) }
    factoryOf(::CheckInApiHelperImpl) bind CheckInApiHelper::class

    single { CheckInLocationApiService(get()) }
    factoryOf(::CheckInLocationApiHelperImpl) bind CheckInLocationApiHelper::class
}

val repositoryModule = module {
    factoryOf(::AppConfigRepositoryImpl) bind AppConfigRepository::class

    singleOf(::UserPresenceRepositoryImpl) bind UserPresenceRepository::class

    singleOf(::AuthRepositoryImpl) bind AuthRepository::class

    factoryOf(::DeviceRepositoryImpl) bind DeviceRepository::class

    singleOf(::SliderRepositoryImpl) bind SliderRepository::class

    singleOf(::FacultyRepositoryImpl) bind FacultyRepository::class

    singleOf(::StudentRepositoryImpl) bind StudentRepository::class

    factoryOf(::TeacherRepositoryImpl) bind TeacherRepository::class

    factoryOf(::DonationRepositoryImpl) bind DonationRepository::class

    factoryOf(::FileHandlerRepositoryImpl) bind FileHandlerRepository::class

    factoryOf(::SupportRepositoryImpl) bind SupportRepository::class

    singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class

    factoryOf(::BloodDonationRepositoryImpl) bind BloodDonationRepository::class

    factoryOf(::BloodDonationRequestRepositoryImpl) bind BloodDonationRequestRepository::class

    factoryOf(::CheckInRepositoryImpl) bind CheckInRepository::class

    factoryOf(::CheckInLocationRepositoryImpl) bind CheckInLocationRepository::class

    factoryOf(::SharedPrefRepositoryImpl) bind SharedPrefRepository::class

    factoryOf(::SystemNotificationRepositoryImpl) bind SystemNotificationRepository::class
    factoryOf(::CustomNotificationRepositoryImpl) bind CustomNotificationRepository::class
}

val dataModule = listOf(
    platformDataModule,
    firebaseModule,
    networkModule,
    repositoryModule,
)
