package com.workfort.pstuian.view.imageslider.indicatorview.animation.type

import android.animation.IntEvaluator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import com.workfort.pstuian.view.imageslider.indicatorview.animation.controller.ValueController
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.type.SwapAnimationValue

class SwapAnimation(listener: ValueController.UpdateListener) : BaseAnimation<ValueAnimator?>(listener) {
    companion object {
        private const val ANIMATION_COORDINATE = "ANIMATION_COORDINATE"
        private const val ANIMATION_COORDINATE_REVERSE = "ANIMATION_COORDINATE_REVERSE"
        private const val COORDINATE_NONE = -1
    }

    private var coordinateStart = COORDINATE_NONE
    private var coordinateEnd = COORDINATE_NONE
    private val value = SwapAnimationValue()
    override fun createAnimator(): ValueAnimator {
        val animator = ValueAnimator()
        animator.duration = DEFAULT_ANIMATION_TIME.toLong()
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { animation -> onAnimateUpdated(animation) }
        return animator
    }

    override fun progress(progress: Float): SwapAnimation {
        if (animator != null) {
            val playTime = (progress * animationDuration).toLong()
            if (animator?.values != null && !animator?.values.isNullOrEmpty()) {
                animator?.currentPlayTime = playTime
            }
        }
        return this
    }

    fun with(coordinateStart: Int, coordinateEnd: Int): SwapAnimation {
        if (animator != null && hasChanges(coordinateStart, coordinateEnd)) {
            this.coordinateStart = coordinateStart
            this.coordinateEnd = coordinateEnd
            val holder =
                createColorPropertyHolder(ANIMATION_COORDINATE, coordinateStart, coordinateEnd)
            val holderReverse = createColorPropertyHolder(
                ANIMATION_COORDINATE_REVERSE,
                coordinateEnd,
                coordinateStart
            )
            animator?.setValues(holder, holderReverse)
        }
        return this
    }

    private fun createColorPropertyHolder(
        propertyName: String,
        startValue: Int,
        endValue: Int
    ): PropertyValuesHolder {
        val holder = PropertyValuesHolder.ofInt(propertyName, startValue, endValue)
        holder.setEvaluator(IntEvaluator())
        return holder
    }

    private fun onAnimateUpdated(animation: ValueAnimator) {
        val coordinate = animation.getAnimatedValue(ANIMATION_COORDINATE) as Int
        val coordinateReverse = animation.getAnimatedValue(ANIMATION_COORDINATE_REVERSE) as Int
        value.coordinate = coordinate
        value.coordinateReverse = coordinateReverse
        listener.onValueUpdated(value)
    }

    private fun hasChanges(coordinateStart: Int, coordinateEnd: Int): Boolean {
        if (this.coordinateStart != coordinateStart) {
            return true
        }
        return this.coordinateEnd != coordinateEnd
    }
}