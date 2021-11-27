package com.workfort.pstuian.util.view.imageslider.indicatorview.animation.type

import android.animation.IntEvaluator
import android.animation.PropertyValuesHolder
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.controller.ValueController.UpdateListener

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 8:52.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/11/27.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

open class ScaleDownAnimation(listener: UpdateListener) : ScaleAnimation(listener) {
    override fun createScalePropertyHolder(isReverse: Boolean): PropertyValuesHolder {
        val propertyName: String
        val startRadiusValue: Int
        val endRadiusValue: Int
        if (isReverse) {
            propertyName = ANIMATION_SCALE_REVERSE
            startRadiusValue = (radius * scaleFactor).toInt()
            endRadiusValue = radius
        } else {
            propertyName = ANIMATION_SCALE
            startRadiusValue = radius
            endRadiusValue = (radius * scaleFactor).toInt()
        }
        val holder = PropertyValuesHolder.ofInt(propertyName, startRadiusValue, endRadiusValue)
        holder.setEvaluator(IntEvaluator())
        return holder
    }
}