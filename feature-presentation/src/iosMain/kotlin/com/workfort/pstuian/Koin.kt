package com.workfort.pstuian

import com.workfort.pstuian.data.di.dataModule
import com.workfort.pstuian.di.featurePresentationModule
import com.workfort.pstuian.featuredomain.di.featureDomainModule
import com.workfort.pstuian.util.di.utilModule
import com.workfort.pstuian.util.PlatformInfo
import com.workfort.pstuian.util.PushNotificationProvider
import com.workfort.pstuian.util.IOSPlatformInfo
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val iosModule = module {
    singleOf(::IOSPlatformInfo) bind PlatformInfo::class
    singleOf(::IosPushNotificationProvider) bind PushNotificationProvider::class
}

fun doInitKoin() {
    startKoin {
        modules(
            featureDomainModule +
                    featurePresentationModule +
                    dataModule +
                    utilModule +
                    iosModule
        )
    }
}
