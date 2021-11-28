package com.workfort.pstuian.util.view.animatedtextview.base

import android.content.res.Resources
import kotlin.math.roundToInt

/**
 * util to get:
 * dp convert px
 * screen's width/height
 */
object DisplayUtils {
    private fun getDisplayMetrics() = Resources.getSystem().displayMetrics

    fun dp2px(dp: Float) = (dp * getDisplayMetrics().density).roundToInt()

    fun getScreenWidth() = getDisplayMetrics().widthPixels

    fun getScreenHeight() = getDisplayMetrics().heightPixels
}