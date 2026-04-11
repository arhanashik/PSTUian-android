package com.workfort.pstuian.sharedpref.di

import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharedPrefPlatformModule = module {
    single<Settings> {
        SharedPreferencesSettings(
            androidContext().getSharedPreferences("pstuian_prefs", 0)
        )
    }
}
