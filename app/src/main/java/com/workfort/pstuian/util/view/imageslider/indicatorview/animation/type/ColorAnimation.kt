package com.workfort.pstuian.util.view.imageslider.indicatorview.animation.type

import android.animation.ArgbEvaluator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.controller.ValueController.UpdateListener
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.type.ColorAnimationValue

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 8:43.
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

open class ColorAnimation(listener: UpdateListener) : BaseAnimation<ValueAnimator?>(listener) {
    companion object {
        const val DEFAULT_UNSELECTED_COLOR = "#33ffffff"
        const val DEFAULT_SELECTED_COLOR = "#ffffff"
        const val ANIMATION_COLOR_REVERSE = "ANIMATION_COLOR_REVERSE"
        const val ANIMATION_COLOR = "ANIMATION_COLOR"
    }

    private val value = ColorAnimationValue()
    var colorStart = 0
    var colorEnd = 0

    override fun createAnimator(): ValueAnimator {
        val animator = ValueAnimator()
        animator.duration = DEFAULT_ANIMATION_TIME.toLong()
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { animation -> onAnimateUpdated(animation) }
        return animator
    }

    override fun progress(progress: Float): ColorAnimation {
        animator?.let {
            val playTime = (progress * animationDuration).toLong()
            if (it.values.isNotEmpty()) {
                it.currentPlayTime = playTime
            }
        }
        return this
    }

    fun with(colorStart: Int, colorEnd: Int): ColorAnimation {
        if (animator != null && hasChanges(colorStart, colorEnd)) {
            this.colorStart = colorStart
            this.colorEnd = colorEnd
            val colorHolder = createColorPropertyHolder(false)
            val reverseColorHolder = createColorPropertyHolder(true)
            animator?.setValues(colorHolder, reverseColorHolder)
        }
        return this
    }

    fun createColorPropertyHolder(isReverse: Boolean): PropertyValuesHolder {
        val propertyName: String
        val colorStart: Int
        val colorEnd: Int
        if (isReverse) {
            propertyName = ANIMATION_COLOR_REVERSE
            colorStart = this.colorEnd
            colorEnd = this.colorStart
        } else {
            propertyName = ANIMATION_COLOR
            colorStart = this.colorStart
            colorEnd = this.colorEnd
        }
        val holder = PropertyValuesHolder.ofInt(propertyName, colorStart, colorEnd)
        holder.setEvaluator(ArgbEvaluator())
        return holder
    }

    private fun hasChanges(colorStart: Int, colorEnd: Int): Boolean {
        if (this.colorStart != colorStart) {
            return true
        }
        return this.colorEnd != colorEnd
    }

    private fun onAnimateUpdated(animation: ValueAnimator) {
        val color = animation.getAnimatedValue(ANIMATION_COLOR) as Int
        val colorReverse = animation.getAnimatedValue(ANIMATION_COLOR_REVERSE) as Int
        value.color = color
        value.colorReverse = colorReverse
        listener.onValueUpdated(value)
    }
}