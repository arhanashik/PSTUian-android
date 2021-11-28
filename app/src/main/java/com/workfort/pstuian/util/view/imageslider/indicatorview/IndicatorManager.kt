package com.workfort.pstuian.util.view.imageslider.indicatorview

import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.AnimationManager
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.controller.ValueController.UpdateListener
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.Value
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.DrawManager
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Indicator

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 6:14.
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

class IndicatorManager internal constructor(private val listener: Listener) :
    UpdateListener {
    private val drawManager = DrawManager()
    private val animationManager = AnimationManager(drawManager.indicator, this)

    internal interface Listener {
        fun onIndicatorUpdated()
    }

    fun animate(): AnimationManager {
        return animationManager
    }

    fun indicator(): Indicator = drawManager.indicator

    fun drawer(): DrawManager = drawManager

    override fun onValueUpdated(value: Value?) {
        drawManager.updateValue(value)
        listener.onIndicatorUpdated()
    }
}