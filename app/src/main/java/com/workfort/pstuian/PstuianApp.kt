package com.workfort.pstuian

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.workfort.pstuian.di.initKoin
import com.workfort.pstuian.util.di.appModules
import com.workfort.pstuian.util.helper.ContextHolder
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import timber.log.Timber

/**
 *  ****************************************************************************
 *  * Created by : Arhan Ashik on 12/11/2018 at 4:18 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  ****************************************************************************
 */

class PstuianApp  : MultiDexApplication() {
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

        if (applicationContext != null) {
            if (BuildConfig.DEBUG) {
                plantTimber()
            }
            initDb(applicationContext)
        }
        triggerKoin()
    }

    // trigger di library koin
    private fun triggerKoin() {
        initKoin {
            androidContext(this@PstuianApp)
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            modules(appModules)
        }
    }

    private fun plantTimber() {
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                return super.createStackElementTag(element) +
                        " - Method:${element.methodName} - Line:${element.lineNumber}"
            }
        })
    }

    private fun initDb(context: Context) {
        ContextHolder.init(context)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}