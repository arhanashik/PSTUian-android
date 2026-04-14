package com.workfort.pstuian.util.di

import com.workfort.pstuian.util.PlatformInfo
import com.workfort.pstuian.util.IOSPlatformInfo
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformUtilModule: Module = module {
    singleOf(::IOSPlatformInfo) bind PlatformInfo::class
}