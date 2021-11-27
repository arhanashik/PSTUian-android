package com.workfort.pstuian.util.view.imageslider.indicatorview.animation.type

import android.animation.Animator
import android.animation.ValueAnimator
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.controller.ValueController.UpdateListener

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 8:37.
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

abstract class BaseAnimation<T : Animator?>(val listener: UpdateListener) {
    companion object {
        const val DEFAULT_ANIMATION_TIME = 350
    }

    protected var animationDuration = DEFAULT_ANIMATION_TIME.toLong()
    protected var animator: T? = createAnimator()

    abstract fun createAnimator(): T
    abstract fun progress(progress: Float): BaseAnimation<*>
    open fun duration(duration: Long): BaseAnimation<*> {
        animationDuration = duration
        if (animator is ValueAnimator) {
            animator?.duration = animationDuration
        }
        return this
    }

    fun start() {
        animator?.apply {
            if(!isRunning) {
                start()
            }
        }
    }

    fun end() {
        animator?.apply {
            if(isStarted) {
                end()
            }
        }
    }
}
