package com.workfort.pstuian.util.helper

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Locale

actual object PlatformUtil {

    actual val deviceVersionName: String = Build.VERSION.RELEASE ?: Build.VERSION.SDK_INT.toString()

    actual val appVersionCode: Int = try {
        val context = ContextHolder.get()
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode.toInt()
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode
        }
    } catch (e: Exception) {
        0
    }

    actual val appVersionName: String = try {
        val context = ContextHolder.get()
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: ""
    } catch (e: Exception) {
        ""
    }

    actual fun getLocaleLanguage(): String = Locale.getDefault().language

    actual fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        if (model.lowercase().startsWith(manufacturer.lowercase())) {
            return capitalize(model)
        }

        return capitalize(manufacturer) + " " + model
    }

    actual fun getLocalIpAddress(): String {
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

    private fun capitalize(s: String?): String {
        if (s.isNullOrEmpty()) {
            return ""
        }
        return if (s[0].isUpperCase()) {
            s
        } else {
            s[0].uppercase() + s.substring(1)
        }
    }

    @Suppress("DEPRECATION")
    actual fun vibrate() {
        val context = ContextHolder.get()
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =  context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
                    as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200,
                VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(longArrayOf(0, 150), -1)
        }
    }
}
