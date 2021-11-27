package com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.type

import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.Value

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 8:20.
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

data class SwapAnimationValue(
    var coordinate: Int = 0,
    var coordinateReverse: Int = 0
) : Value