package com.workfort.pstuian.app.platform

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import com.workfort.pstuian.BuildConfig
import com.workfort.pstuian.util.PlatformInfo
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Locale
import java.util.UUID

class AndroidPlatformInfo(private val context: Context) : PlatformInfo {
    override val appVersion: String
        get() = try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: BuildConfig.VERSION_NAME
        } catch (_: Exception) {
            BuildConfig.VERSION_NAME
        }

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

    override val platformName: String
        get() = "Android ${Build.VERSION.RELEASE}"

    override val locale: String
        get() = Locale.getDefault().language

    override fun getLocalIpAddress(): String {
        try {
            val en = NetworkInterface.getNetworkInterfaces()

            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()

                    if(!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.hostAddress?: ""
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }

        return ""
    }

//    override fun vibrate() {
//        val context = ContextHolder.get()
//        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val vibratorManager =  context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
//                    as VibratorManager
//            vibratorManager.defaultVibrator
//        } else {
//            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            vibrator.vibrate(VibrationEffect.createOneShot(200,
//                VibrationEffect.DEFAULT_AMPLITUDE))
//        } else {
//            vibrator.vibrate(longArrayOf(0, 150), -1)
//        }
//    }
}
