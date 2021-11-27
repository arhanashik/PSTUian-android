package com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data

import android.view.View
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.type.IndicatorAnimationType

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 6:54.
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

data class Indicator(
    var height: Int = 0,
    var width: Int = 0,
    var radius: Int = 0,
    var padding: Int = 0,
    var paddingLeft: Int = 0,
    var paddingTop: Int = 0,
    var paddingRight: Int = 0,
    var paddingBottom: Int = 0,
    var stroke: Int = 0, //For "Fill" animation only
    var scaleFactor: Float = 0f, //For "Scale" animation only
    var unselectedColor: Int = 0,
    var selectedColor: Int = 0,
    var interactiveAnimation: Boolean = false,
    var autoVisibility: Boolean = false,
    var dynamicCount: Boolean = false,
    var animationDuration: Long = 0,
    var count: Int = DEFAULT_COUNT,
    var selectedPosition: Int = 0,
    var selectingPosition: Int = 0,
    var lastSelectedPosition: Int = 0,
    var viewPagerId: Int = View.NO_ID,
) {
    companion object {
        const val DEFAULT_COUNT = 3
        const val MIN_COUNT = 1
        const val COUNT_NONE = -1
        const val DEFAULT_RADIUS_DP = 6
        const val DEFAULT_PADDING_DP = 8
    }
    var orientation: Orientation? = null
        get() {
            if (field == null) field = Orientation.HORIZONTAL
            return field
        }
    var animationType: IndicatorAnimationType? = null
        get() {
            if (field == null) field = IndicatorAnimationType.NONE
            return field
        }
    var rtlMode: RtlMode? = null
        get() {
            if (field == null) field = RtlMode.Off
            return field
        }
}