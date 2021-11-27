package com.workfort.pstuian.util.view.imageslider.indicatorview.animation.type

import android.animation.IntEvaluator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.controller.ValueController.UpdateListener
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.type.SlideAnimationValue

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 8:55.
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

class SlideAnimation(listener: UpdateListener) : BaseAnimation<ValueAnimator?>(listener) {
    companion object {
        private const val ANIMATION_COORDINATE = "ANIMATION_COORDINATE"
        private const val COORDINATE_NONE = -1
    }

    private val value = SlideAnimationValue()
    private var coordinateStart = COORDINATE_NONE
    private var coordinateEnd = COORDINATE_NONE
    override fun createAnimator(): ValueAnimator {
        val animator = ValueAnimator()
        animator.duration = DEFAULT_ANIMATION_TIME.toLong()
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { animation -> onAnimateUpdated(animation) }
        return animator
    }

    override fun progress(progress: Float): SlideAnimation {
        if (animator != null) {
            val playTime = (progress * animationDuration).toLong()
            if (animator?.values != null && !animator?.values.isNullOrEmpty()) {
                animator?.currentPlayTime = playTime
            }
        }
        return this
    }

    fun with(coordinateStart: Int, coordinateEnd: Int): SlideAnimation {
        if (animator != null && hasChanges(coordinateStart, coordinateEnd)) {
            this.coordinateStart = coordinateStart
            this.coordinateEnd = coordinateEnd
            val holder = createSlidePropertyHolder()
            animator?.setValues(holder)
        }
        return this
    }

    private fun createSlidePropertyHolder(): PropertyValuesHolder {
        val holder =
            PropertyValuesHolder.ofInt(ANIMATION_COORDINATE, coordinateStart, coordinateEnd)
        holder.setEvaluator(IntEvaluator())
        return holder
    }

    private fun onAnimateUpdated(animation: ValueAnimator) {
        val coordinate = animation.getAnimatedValue(ANIMATION_COORDINATE) as Int
        value.coordinate = coordinate
        listener.onValueUpdated(value)
    }

    private fun hasChanges(coordinateStart: Int, coordinateEnd: Int): Boolean {
        if (this.coordinateStart != coordinateStart) {
            return true
        }
        return this.coordinateEnd != coordinateEnd
    }
}