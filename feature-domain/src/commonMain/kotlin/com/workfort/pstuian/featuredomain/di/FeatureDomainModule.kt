package com.workfort.pstuian.featuredomain.di

import com.workfort.pstuian.featuredomain.usecase.ClearAllDataUseCase
import com.workfort.pstuian.featuredomain.usecase.RegisterDeviceUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val useCaseModule = module {
    factoryOf(::ClearAllDataUseCase)
    factoryOf(::RegisterDeviceUseCase)
}

val featureDomainModule = listOf(useCaseModule)