package com.workfort.pstuian

import com.workfort.pstuian.util.IOSPlatformInfo
import com.workfort.pstuian.util.PlatformInfo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


object IOSBridge {

    fun initializeKoin() {
        val platformModule = module {
            singleOf(::IOSPlatformInfo) bind PlatformInfo::class
        }
    }
}