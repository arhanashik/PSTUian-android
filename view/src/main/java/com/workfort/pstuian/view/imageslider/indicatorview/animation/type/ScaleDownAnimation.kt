package com.workfort.pstuian.view.imageslider.indicatorview.animation.type

import android.animation.IntEvaluator
import android.animation.PropertyValuesHolder
import com.workfort.pstuian.view.imageslider.indicatorview.animation.controller.ValueController

open class ScaleDownAnimation(listener: ValueController.UpdateListener) : ScaleAnimation(listener) {
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