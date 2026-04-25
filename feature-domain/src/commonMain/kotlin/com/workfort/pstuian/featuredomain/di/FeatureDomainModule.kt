package com.workfort.pstuian.featuredomain.di

import com.workfort.pstuian.featuredomain.framework.coroutine.AppCoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.usecase.ClearCacheUseCase
import com.workfort.pstuian.featuredomain.usecase.GetEmployeeProfileUserUseCase
import com.workfort.pstuian.featuredomain.usecase.GetInitialScreenUseCase
import com.workfort.pstuian.featuredomain.usecase.GetSignedInUserUseCase
import com.workfort.pstuian.featuredomain.usecase.GetStudentProfileUserUseCase
import com.workfort.pstuian.featuredomain.usecase.GetTeacherProfileUserUseCase
import com.workfort.pstuian.featuredomain.usecase.RegisterDeviceUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val frameworkModule = module {
    factoryOf(::AppCoroutineDispatcherProvider) bind CoroutineDispatcherProvider::class
}

private val useCaseModule = module {
    factoryOf(::ClearCacheUseCase)
    factoryOf(::GetInitialScreenUseCase)
    factoryOf(::GetSignedInUserUseCase)
    factoryOf(::GetStudentProfileUserUseCase)
    factoryOf(::GetTeacherProfileUserUseCase)
    factoryOf(::GetEmployeeProfileUserUseCase)
    factoryOf(::RegisterDeviceUseCase)
}

val featureDomainModule = listOf(frameworkModule, useCaseModule)