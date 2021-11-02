package com.workfort.pstuian.util.helper

import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import com.workfort.pstuian.BuildConfig
import com.workfort.pstuian.PstuianApp
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*
import kotlin.random.Random

/**
 *  ****************************************************************************
 *  * Created by : arhan on 28 Oct, 2021 at 13:38.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/10/28.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

object AndroidUtil {
    val randomColor: Int = Color.argb(
        255, Random.nextInt(256),
        Random.nextInt(256),
        Random.nextInt(256)
    )

    const val getVersionCode = BuildConfig.VERSION_CODE
    const val getVersionName = BuildConfig.VERSION_NAME
    val getDeviceVersionName: String = Build.VERSION.RELEASE?: Build.VERSION.SDK_INT.toString()

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
        if (s == null || s.isEmpty()) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first) + s.substring(1)
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

    fun generateGradientDrawable(
        colors: ArrayList<String>,
        orientationStr: String = "BL_TR"
    ): GradientDrawable {
        val colorsInt = IntArray(colors.size)

        for(i in 0 until colors.size) {
            val colorStr = colors[i]
            colorsInt[i] = Color.parseColor(colorStr)
        }

        val orientation = GradientDrawable.Orientation.valueOf(orientationStr)

        return generateGradientDrawable(colorsInt, orientation)
    }

    fun generateGradientDrawable(
        colors: IntArray,
        orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.BL_TR
    ): GradientDrawable {
        return GradientDrawable(orientation, colors).apply {
            cornerRadius = 0f
        }
    }

    fun setBackgroundAnimated(
        root: View,
        newBackground: Drawable,
        durationMills: Int = 250
    ) {
        val currentBG: Drawable? = root.background

        if (currentBG == null) {
            root.background = newBackground
        } else {
            val transitionDrawable = TransitionDrawable(arrayOf(currentBG, newBackground))
//            transitionDrawable.isCrossFadeEnabled = true

            root.background = transitionDrawable
            transitionDrawable.startTransition(durationMills)
        }
    }
}