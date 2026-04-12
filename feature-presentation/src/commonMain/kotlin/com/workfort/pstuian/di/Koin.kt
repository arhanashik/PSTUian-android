package com.workfort.pstuian.di

import com.workfort.pstuian.database.di.databaseModule
import com.workfort.pstuian.di.useCaseModule
import com.workfort.pstuian.repository.di.repositoryModule
import com.workfort.pstuian.sharedpref.di.sharedPrefModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            platformModule(),
            databaseModule,
            sharedPrefModule,
            repositoryModule,
            useCaseModule,
            stateReducerModule,
            viewModelModule,
        )
    }

// called by iOS etc
fun initKoin() = initKoin {}

expect fun platformModule(): Module
