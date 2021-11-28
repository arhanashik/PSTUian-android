package com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.type

import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.Value

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 8:17.
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

data class ScaleAnimationValue(
    var color: Int = 0,
    var colorReverse: Int = 0,
    var radius: Int = 0,
    var radiusReverse: Int = 0,
) : Value