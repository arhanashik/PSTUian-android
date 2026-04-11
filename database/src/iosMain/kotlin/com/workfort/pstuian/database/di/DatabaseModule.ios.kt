package com.workfort.pstuian.database.di

import com.workfort.pstuian.database.getDatabaseBuilder
import org.koin.dsl.module

val databasePlatformModule = module {
    single { getDatabaseBuilder() }
}
