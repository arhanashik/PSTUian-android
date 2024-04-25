package com.workfort.pstuian.util.helper

import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.workfort.pstuian.PstuianApp
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Locale

object AndroidUtil {

    val getDeviceVersionName: String = Build.VERSION.RELEASE ?: Build.VERSION.SDK_INT.toString()

    fun getLocaleLanguage(): String = Locale.getDefault().language

    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        if (model.lowercase().startsWith(manufacturer.lowercase())) {
            return capitalize(model)
        }

        return capitalize(manufacturer) + " " + model
    }

    fun getLocalIpAddress(): String {
        try {
            val en = NetworkInterface.getNetworkInterfaces()

            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()

                    if(inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
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
        return if (Character.isUpperCase(s.first())) {
            s
        } else {
            Character.toUpperCase(s.first()) + s.substring(1)
        }
    }

    @Suppress("DEPRECATION")
    fun vibrate() {
        val context = PstuianApp.getBaseApplicationContext()
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =  context.getSystemService(VIBRATOR_MANAGER_SERVICE)
                    as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200,
                VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(longArrayOf(0, 150), -1)
        }
    }
}