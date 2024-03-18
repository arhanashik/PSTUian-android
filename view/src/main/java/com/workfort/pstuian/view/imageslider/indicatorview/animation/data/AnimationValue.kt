package com.workfort.pstuian.view.imageslider.indicatorview.animation.data

import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.type.ColorAnimationValue
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.type.DropAnimationValue
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.type.FillAnimationValue
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.type.ScaleAnimationValue
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.type.SwapAnimationValue
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.type.ThinWormAnimationValue
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.type.WormAnimationValue

class AnimationValue {
    var colorAnimationValue: ColorAnimationValue = ColorAnimationValue()
    var scaleAnimationValue: ScaleAnimationValue = ScaleAnimationValue()
    var wormAnimationValue: WormAnimationValue = WormAnimationValue()
    var fillAnimationValue: FillAnimationValue = FillAnimationValue()
    var thinWormAnimationValue: ThinWormAnimationValue = ThinWormAnimationValue()
    var dropAnimationValue: DropAnimationValue = DropAnimationValue()
    var swapAnimationValue: SwapAnimationValue = SwapAnimationValue()
}