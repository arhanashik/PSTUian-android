package com.workfort.pstuian.util.view.imageslider.indicatorview.animation

import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.controller.AnimationController
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.controller.ValueController.UpdateListener
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Indicator

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 8:04.
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

class AnimationManager(indicator: Indicator, listener: UpdateListener) {
    private val animationController = AnimationController(indicator, listener)
    fun basic() {
        animationController.end()
        animationController.basic()
    }
    fun interactive(progress: Float) = animationController.interactive(progress)
    fun end() = animationController.end()
}