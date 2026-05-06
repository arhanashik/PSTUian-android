package com.workfort.pstuian

import android.app.Application
import android.content.Context
import com.workfort.pstuian.app.di.appModule
import com.workfort.pstuian.data.di.dataModule
import com.workfort.pstuian.di.featurePresentationModule
import com.workfort.pstuian.featuredomain.di.featureDomainModule
import com.workfort.pstuian.util.Logger
import com.workfort.pstuian.util.di.utilModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PstuianApp  : Application() {

    init {
        sInstance = this
    }

    companion object {
        private lateinit var sInstance: PstuianApp

        fun getBaseApplicationContext(): Context {
            return sInstance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Logger
        if (BuildConfig.DEBUG) {
            Logger.init()
        }

        triggerKoin()
    }

    // trigger di library koin
    private fun triggerKoin() {
        startKoin {
            androidContext(this@PstuianApp)
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            modules(koinModules)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    private val koinModules = appModule +
            featureDomainModule +
            featurePresentationModule +
            dataModule +
            utilModule
}