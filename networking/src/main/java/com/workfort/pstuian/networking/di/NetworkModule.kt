package com.workfort.pstuian.networking.di

import com.workfort.pstuian.networking.RetrofitBuilder
import org.koin.dsl.module

val networkModule = module {
    single { RetrofitBuilder.createAuthApiService() }
    single { RetrofitBuilder.createSliderApiService() }
    single { RetrofitBuilder.createFacultyApiService() }
    single { RetrofitBuilder.createStudentApiService() }
    single { RetrofitBuilder.createTeacherApiService() }
    single { RetrofitBuilder.createDonationApiService() }
    single { RetrofitBuilder.createFileHandlerApiService() }
    single { RetrofitBuilder.createSupportApiService() }
    single { RetrofitBuilder.createNotificationApiService() }
    single { RetrofitBuilder.createBloodDonationApiService() }
    single { RetrofitBuilder.createBloodDonationRequestApiService() }
    single { RetrofitBuilder.createCheckInApiService() }
    single { RetrofitBuilder.createCheckInLocationApiService() }
}