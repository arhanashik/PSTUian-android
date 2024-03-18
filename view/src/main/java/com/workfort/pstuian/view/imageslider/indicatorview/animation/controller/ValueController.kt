package com.workfort.pstuian.view.imageslider.indicatorview.animation.controller

import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.Value
import com.workfort.pstuian.view.imageslider.indicatorview.animation.type.ColorAnimation
import com.workfort.pstuian.view.imageslider.indicatorview.animation.type.DropAnimation
import com.workfort.pstuian.view.imageslider.indicatorview.animation.type.FillAnimation
import com.workfort.pstuian.view.imageslider.indicatorview.animation.type.ScaleAnimation
import com.workfort.pstuian.view.imageslider.indicatorview.animation.type.ScaleDownAnimation
import com.workfort.pstuian.view.imageslider.indicatorview.animation.type.SlideAnimation
import com.workfort.pstuian.view.imageslider.indicatorview.animation.type.SwapAnimation
import com.workfort.pstuian.view.imageslider.indicatorview.animation.type.ThinWormAnimation
import com.workfort.pstuian.view.imageslider.indicatorview.animation.type.WormAnimation

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