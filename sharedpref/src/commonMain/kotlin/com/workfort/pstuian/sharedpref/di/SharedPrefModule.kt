package com.workfort.pstuian.sharedpref.di

import com.workfort.pstuian.sharedpref.Prefs
import org.koin.dsl.module

val sharedPrefModule = module {
    single { Prefs(get()) }
}
