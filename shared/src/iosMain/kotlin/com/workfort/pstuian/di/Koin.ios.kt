package com.workfort.pstuian.di

import com.workfort.pstuian.database.di.databasePlatformModule
import com.workfort.pstuian.sharedpref.di.sharedPrefPlatformModule
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    includes(databasePlatformModule, sharedPrefPlatformModule)
}
