package com.workfort.pstuian.app.di

import com.workfort.pstuian.app.firebase.FcmCallback
import com.workfort.pstuian.app.platform.AndroidPlatformInfo
import com.workfort.pstuian.app.platform.AndroidPushNotificationProvider
import com.workfort.pstuian.util.PlatformInfo
import com.workfort.pstuian.util.PushNotificationProvider
import com.workfort.pstuian.app.firebase.FcmCallbackImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val platformModule = module {
    singleOf(::AndroidPlatformInfo) bind PlatformInfo::class
    singleOf(::AndroidPushNotificationProvider) bind PushNotificationProvider::class
}

val firebaseModule = module {
    singleOf(::FcmCallbackImpl) bind FcmCallback::class
}

val appModule = listOf(platformModule, firebaseModule)
