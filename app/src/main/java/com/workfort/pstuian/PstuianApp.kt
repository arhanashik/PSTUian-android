package com.workfort.pstuian

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.workfort.pstuian.app.data.local.database.AppDatabase
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.util.lib.koin.repositoryModule
import com.workfort.pstuian.util.lib.koin.networkModule
import com.workfort.pstuian.util.lib.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

/**
 *  ****************************************************************************
 *  * Created by : Arhan Ashik on 12/11/2018 at 4:18 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * Last edited by : Arhan Ashik on 12/11/2018.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
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

        triggerKoin()
        if (applicationContext != null) {
            if (BuildConfig.DEBUG) {
                plantTimber()
            }
            initDb(applicationContext)
        }
    }

    //trigger di library koin
    private fun triggerKoin() {
        startKoin {
            androidContext(this@PstuianApp)
            androidLogger(Level.DEBUG)
            modules(viewModelModule, networkModule, repositoryModule)
        }
    }

    private fun plantTimber() {
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                return super.createStackElementTag(element) +
                        " - Method:${element.methodName} - Line:${element.lineNumber}"
            }
        })
    }

    private fun initDb(context: Context) {
        Prefs.init(context)
        AppDatabase.getDatabase()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}