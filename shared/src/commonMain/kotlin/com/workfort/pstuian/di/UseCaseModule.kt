package com.workfort.pstuian.di

import com.workfort.pstuian.usecase.ClearAllDataUseCase
import com.workfort.pstuian.usecase.RegisterDeviceUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { ClearAllDataUseCase(get(), get(), get(), get()) }
    factory { RegisterDeviceUseCase(get()) }
}
