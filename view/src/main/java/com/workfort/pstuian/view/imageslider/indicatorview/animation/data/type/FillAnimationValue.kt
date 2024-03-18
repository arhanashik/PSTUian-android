package com.workfort.pstuian.view.imageslider.indicatorview.animation.data.type

import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.Value

data class FillAnimationValue(
    var color: Int = 0,
    var colorReverse: Int = 0,
    var radius: Int = 0,
    var radiusReverse: Int = 0,
    var stroke: Int = 0,
    var strokeReverse: Int = 0
) : Value