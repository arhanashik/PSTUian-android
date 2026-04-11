package com.workfort.pstuian.networking.di

import com.workfort.pstuian.networking.KtorClientFactory
import com.workfort.pstuian.networking.service.AuthApiService
import com.workfort.pstuian.networking.service.BloodDonationApiService
import com.workfort.pstuian.networking.service.BloodDonationRequestApiService
import com.workfort.pstuian.networking.service.CheckInApiService
import com.workfort.pstuian.networking.service.CheckInLocationApiService
import com.workfort.pstuian.networking.service.DonationApiService
import com.workfort.pstuian.networking.service.FacultyApiService
import com.workfort.pstuian.networking.service.FileHandlerApiService
import com.workfort.pstuian.networking.service.NotificationApiService
import com.workfort.pstuian.networking.service.SliderApiService
import com.workfort.pstuian.networking.service.StudentApiService
import com.workfort.pstuian.networking.service.SupportApiService
import com.workfort.pstuian.networking.service.TeacherApiService
import com.workfort.pstuian.sharedpref.Prefs
import org.koin.dsl.module

val networkModule = module {
    single { KtorClientFactory.create(authTokenProvider = { get<Prefs>().authToken }) }
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
