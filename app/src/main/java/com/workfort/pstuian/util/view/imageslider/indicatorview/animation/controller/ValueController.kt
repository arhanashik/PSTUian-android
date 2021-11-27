package com.workfort.pstuian.util.view.imageslider.indicatorview.animation.controller

import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.Value
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.type.*

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 8:28.
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

class ValueController(listener: UpdateListener) {
    private var colorAnimation: ColorAnimation = ColorAnimation(listener)
    private var scaleAnimation: ScaleAnimation = ScaleAnimation(listener)
    private var wormAnimation: WormAnimation = WormAnimation(listener)
    private var slideAnimation: SlideAnimation = SlideAnimation(listener)
    private var fillAnimation: FillAnimation = FillAnimation(listener)
    private var thinWormAnimation: ThinWormAnimation = ThinWormAnimation(listener)
    private var dropAnimation: DropAnimation = DropAnimation(listener)
    private var swapAnimation: SwapAnimation = SwapAnimation(listener)
    private var scaleDownAnimation: ScaleDownAnimation = ScaleDownAnimation(listener)

    interface UpdateListener {
        fun onValueUpdated(value: Value?)
    }

    fun color(): ColorAnimation = colorAnimation
    fun scale(): ScaleAnimation = scaleAnimation
    fun worm(): WormAnimation = wormAnimation
    fun slide(): SlideAnimation = slideAnimation
    fun fill(): FillAnimation = fillAnimation
    fun thinWorm(): ThinWormAnimation = thinWormAnimation
    fun drop(): DropAnimation = dropAnimation
    fun swap(): SwapAnimation = swapAnimation
    fun scaleDown(): ScaleDownAnimation = scaleDownAnimation
}