package com.workfort.pstuian.app.platform

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import com.workfort.pstuian.BuildConfig
import com.workfort.pstuian.util.PlatformInfo
import java.util.Locale
import java.util.UUID

class AndroidPlatformInfo(private val context: Context) : PlatformInfo {
    override val appVersionCode: Int
        get() = BuildConfig.VERSION_CODE

    override val appVersionName: String
        get() = BuildConfig.VERSION_NAME

    override val isDebug: Boolean
        get() = BuildConfig.DEBUG

    override val storeUrl: String
        get() = "market://details?id=${context.packageName}"

    override val deviceId: String
        @SuppressLint("HardwareIds")
        get() = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID,
        ) ?: UUID.randomUUID().toString()

    override val model: String
        get() = "${Build.MANUFACTURER} ${Build.MODEL}"

    override val platform: String
        get() = "Android ${Build.VERSION.RELEASE}"

    override val locale: String
        get() = Locale.getDefault().language
}
