package com.workfort.pstuian

import androidx.compose.ui.window.ComposeUIViewController
import com.workfort.pstuian.data.di.dataModule
import com.workfort.pstuian.di.featurePresentationModule
import com.workfort.pstuian.featuredomain.di.featureDomainModule
import com.workfort.pstuian.ui.common.navigation.AppNavHost
import com.workfort.pstuian.util.IOSPlatformInfo
import com.workfort.pstuian.util.PlatformInfo
import com.workfort.pstuian.util.PushNotificationProvider
import com.workfort.pstuian.util.di.utilModule
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import platform.UIKit.UIViewController

class IOSBridge : KoinComponent {

    fun initializeKoin() {
        val iosModule = module {
            singleOf(::IOSPlatformInfo) bind PlatformInfo::class
            singleOf(::IosPushNotificationProvider) bind PushNotificationProvider::class
        }
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

    fun mainViewController(): UIViewController = ComposeUIViewController(
        configure = {
            enforceStrictPlistSanityCheck = false
        }
    ) {
        AppNavHost()
    }
}
