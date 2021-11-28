package com.workfort.pstuian.util.view.imageslider.indicatorview.draw.controller

import android.util.Pair
import android.view.View
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.type.IndicatorAnimationType
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Indicator
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Orientation

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 7:18.
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

object MeasureController {
    fun measureViewSize(
        indicator: Indicator,
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ): Pair<Int, Int> {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val count = indicator.count
        val radius = indicator.radius
        val stroke = indicator.stroke
        val padding = indicator.padding
        val paddingLeft = indicator.paddingLeft
        val paddingTop = indicator.paddingTop
        val paddingRight = indicator.paddingRight
        val paddingBottom = indicator.paddingBottom
        val circleDiameterPx = radius * 2
        var desiredWidth = 0
        var desiredHeight = 0
        var width: Int
        var height: Int
        val orientation = indicator.orientation
        if (count != 0) {
            val diameterSum = circleDiameterPx * count
            val strokeSum = stroke * 2 * count
            val paddingSum = padding * (count - 1)
            val w = diameterSum + strokeSum + paddingSum
            val h = circleDiameterPx + stroke
            if (orientation === Orientation.HORIZONTAL) {
                desiredWidth = w
                desiredHeight = h
            } else {
                desiredWidth = h
                desiredHeight = w
            }
        }
        if (indicator.animationType == IndicatorAnimationType.DROP) {
            if (orientation === Orientation.HORIZONTAL) {
                desiredHeight *= 2
            } else {
                desiredWidth *= 2
            }
        }
        val horizontalPadding = paddingLeft + paddingRight
        val verticalPadding = paddingTop + paddingBottom
        desiredWidth += horizontalPadding
        desiredHeight += verticalPadding
        width = when (widthMode) {
            View.MeasureSpec.EXACTLY -> widthSize
            View.MeasureSpec.AT_MOST -> desiredWidth.coerceAtMost(widthSize)
            else -> desiredWidth
        }
        height = when (heightMode) {
            View.MeasureSpec.EXACTLY -> heightSize
            View.MeasureSpec.AT_MOST -> desiredHeight.coerceAtMost(heightSize)
            else -> desiredHeight
        }
        if (width < 0) {
            width = 0
        }
        if (height < 0) {
            height = 0
        }
        indicator.width = width
        indicator.height = height
        return Pair(width, height)
    }
}