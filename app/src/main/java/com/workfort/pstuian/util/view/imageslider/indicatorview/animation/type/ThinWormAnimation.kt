package com.workfort.pstuian.util.view.imageslider.indicatorview.animation.type

import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.controller.ValueController.UpdateListener
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.type.ThinWormAnimationValue
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.type.WormAnimationValue

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 9:00.
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

class ThinWormAnimation(listener: UpdateListener) : WormAnimation(listener) {
    private val value = ThinWormAnimationValue()
    override fun duration(duration: Long): ThinWormAnimation {
        super.duration(duration)
        return this
    }

    override fun with(
        coordinateStart: Int,
        coordinateEnd: Int,
        radius: Int,
        isRightSide: Boolean
    ): WormAnimation {
        if (hasChanges(coordinateStart, coordinateEnd, radius, isRightSide)) {
            animator = createAnimator()
            this.coordinateStart = coordinateStart
            this.coordinateEnd = coordinateEnd
            this.radius = radius
            this.isRightSide = isRightSide
            val height = radius * 2
            rectLeftEdge = coordinateStart - radius
            rectRightEdge = coordinateStart + radius
            value.rectStart = rectLeftEdge
            value.rectEnd = rectRightEdge
            value.height = height
            val rec = createRectValues(isRightSide)
            val sizeDuration = (animationDuration * 0.8).toLong()
            val reverseDelay = (animationDuration * 0.2).toLong()
            val heightDuration = (animationDuration * 0.5).toLong()
            val reverseHeightDelay = (animationDuration * 0.5).toLong()
            val wormAnimationValue = WormAnimationValue(value.rectStart, value.rectEnd)
            val straightAnimator =
                createWormAnimator(rec.fromX, rec.toX, sizeDuration, false, wormAnimationValue)
            val reverseAnimator = createWormAnimator(
                rec.reverseFromX,
                rec.reverseToX,
                sizeDuration,
                true,
                wormAnimationValue
            )
            reverseAnimator.startDelay = reverseDelay
            val straightHeightAnimator = createHeightAnimator(height, radius, heightDuration)
            val reverseHeightAnimator = createHeightAnimator(radius, height, heightDuration)
            reverseHeightAnimator.startDelay = reverseHeightDelay
            animator?.playTogether(
                straightAnimator,
                reverseAnimator,
                straightHeightAnimator,
                reverseHeightAnimator
            )
        }
        return this
    }

    private fun createHeightAnimator(
        fromHeight: Int,
        toHeight: Int,
        duration: Long
    ): ValueAnimator {
        val anim = ValueAnimator.ofInt(fromHeight, toHeight)
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.duration = duration
        anim.addUpdateListener { animation -> onAnimateUpdated(animation) }
        return anim
    }

    private fun onAnimateUpdated(animation: ValueAnimator) {
        value.height = animation.animatedValue as Int
        listener.onValueUpdated(value)
    }

    override fun progress(progress: Float): ThinWormAnimation {
        animator?.let { animator ->
            val progressDuration = (progress * animationDuration).toLong()
            val size: Int = animator.childAnimations?.size?: 0
            for (i in 0 until size) {
                val anim = animator.childAnimations[i] as ValueAnimator
                var setDuration = progressDuration - anim.startDelay
                val duration = anim.duration
                if (setDuration > duration) {
                    setDuration = duration
                } else if (setDuration < 0) {
                    setDuration = 0
                }
                if (i == size - 1 && setDuration <= 0) {
                    continue
                }
                if (anim.values != null && anim.values.isNotEmpty()) {
                    anim.currentPlayTime = setDuration
                }
            }
        }
        return this
    }
}