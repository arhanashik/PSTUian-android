package com.workfort.pstuian.view.imageslider.indicatorview.animation

import com.workfort.pstuian.view.imageslider.indicatorview.animation.controller.AnimationController
import com.workfort.pstuian.view.imageslider.indicatorview.animation.controller.ValueController
import com.workfort.pstuian.view.imageslider.indicatorview.draw.data.Indicator

class AnimationManager(indicator: Indicator, listener: ValueController.UpdateListener) {
    private val animationController = AnimationController(indicator, listener)
    fun basic() {
        animationController.end()
        animationController.basic()
    }
    fun interactive(progress: Float) = animationController.interactive(progress)
    fun end() = animationController.end()
}