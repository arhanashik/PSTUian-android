package com.workfort.pstuian.util.di

import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformUtilModule: Module = module {
    // AndroidPlatformInfo is now provided from the :app module
    // Remove this once the IOSPlatformInfo is moved as well
}