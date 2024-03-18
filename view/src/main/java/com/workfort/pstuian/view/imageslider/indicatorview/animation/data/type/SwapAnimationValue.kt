package com.workfort.pstuian.view.imageslider.indicatorview.animation.data.type

import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.Value

data class SwapAnimationValue(
    var coordinate: Int = 0,
    var coordinateReverse: Int = 0
) : Value