package com.workfort.pstuian.util.di

import com.workfort.pstuian.firebase.fcm.callback.FcmCallback
import com.workfort.pstuian.util.service.FcmCallbackImpl
import org.koin.dsl.module


val firebaseModule = module {
    single<FcmCallback> {
        FcmCallbackImpl(
            authRepo = get(),
            settingsRepo = get(),
        )
    }
}