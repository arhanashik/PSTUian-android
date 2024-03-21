package com.workfort.pstuian.view.imageslider.indicatorview.animation.type

import android.animation.Animator
import android.animation.ValueAnimator
import com.workfort.pstuian.view.imageslider.indicatorview.animation.controller.ValueController

abstract class BaseAnimation<T : Animator?>(val listener: ValueController.UpdateListener) {
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
