package com.workfort.pstuian.util.view.imageslider.indicatorview.utils

import android.util.Pair
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.type.IndicatorAnimationType
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Indicator
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Orientation

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 6:22.
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

object CoordinatesUtils {
    fun getCoordinate(indicator: Indicator?, position: Int): Int {
        if (indicator == null) {
            return 0
        }
        return if (indicator.orientation == Orientation.HORIZONTAL) {
            getXCoordinate(indicator, position)
        } else {
            getYCoordinate(indicator, position)
        }
    }

    fun getXCoordinate(indicator: Indicator?, position: Int): Int {
        if (indicator == null) {
            return 0
        }
        var coordinate: Int = if (indicator.orientation == Orientation.HORIZONTAL) {
            getHorizontalCoordinate(indicator, position)
        } else {
            getVerticalCoordinate(indicator)
        }
        coordinate += indicator.paddingLeft
        return coordinate
    }

    fun getYCoordinate(indicator: Indicator?, position: Int): Int {
        if (indicator == null) {
            return 0
        }
        var coordinate: Int = if (indicator.orientation == Orientation.HORIZONTAL) {
            getVerticalCoordinate(indicator)
        } else {
            getHorizontalCoordinate(indicator, position)
        }
        coordinate += indicator.paddingTop
        return coordinate
    }

    fun getPosition(indicator: Indicator?, x: Float, y: Float): Int {
        if (indicator == null) {
            return -1
        }
        val lengthCoordinate: Float
        val heightCoordinate: Float
        if (indicator.orientation == Orientation.HORIZONTAL) {
            lengthCoordinate = x
            heightCoordinate = y
        } else {
            lengthCoordinate = y
            heightCoordinate = x
        }
        return getFitPosition(indicator, lengthCoordinate, heightCoordinate)
    }

    private fun getFitPosition(
        indicator: Indicator,
        lengthCoordinate: Float,
        heightCoordinate: Float
    ): Int {
        val count = indicator.count
        val radius = indicator.radius
        val stroke = indicator.stroke
        val padding = indicator.padding
        val height =
            if (indicator.orientation == Orientation.HORIZONTAL) indicator.height else indicator.width
        var length = 0
        for (i in 0 until count) {
            val indicatorPadding = if (i > 0) padding else padding / 2
            val startValue = length
            length += radius * 2 + stroke / 2 + indicatorPadding
            val endValue = length
            val fitLength = lengthCoordinate >= startValue && lengthCoordinate <= endValue
            val fitHeight = heightCoordinate >= 0 && heightCoordinate <= height
            if (fitLength && fitHeight) {
                return i
            }
        }
        return -1
    }

    private fun getHorizontalCoordinate(indicator: Indicator, position: Int): Int {
        val count = indicator.count
        val radius = indicator.radius
        val stroke = indicator.stroke
        val padding = indicator.padding
        var coordinate = 0
        for (i in 0 until count) {
            coordinate += radius + stroke / 2
            if (position == i) {
                return coordinate
            }
            coordinate += radius + padding + stroke / 2
        }
        if (indicator.animationType == IndicatorAnimationType.DROP) {
            coordinate += radius * 2
        }
        return coordinate
    }

    private fun getVerticalCoordinate(indicator: Indicator): Int {
        val radius = indicator.radius
        return if (indicator.animationType == IndicatorAnimationType.DROP) {
            radius * 3
        } else radius
    }

    fun getProgress(
        indicator: Indicator,
        position: Int,
        positionOffset: Float,
        isRtl: Boolean
    ): Pair<Int, Float> {
        val count = indicator.count
        var selectedPosition = indicator.selectedPosition
        var calculatedPosition = if (isRtl) count - 1 - position else position
        if (calculatedPosition < 0) {
            calculatedPosition = 0
        } else if (calculatedPosition > count - 1) {
            calculatedPosition = count - 1
        }
        val isRightOverScrolled = calculatedPosition > selectedPosition
        val isLeftOverScrolled: Boolean = if (isRtl) {
            calculatedPosition - 1 < selectedPosition
        } else {
            calculatedPosition + 1 < selectedPosition
        }
        if (isRightOverScrolled || isLeftOverScrolled) {
            selectedPosition = calculatedPosition
            indicator.selectedPosition = selectedPosition
        }
        val slideToRightSide = selectedPosition == calculatedPosition && positionOffset != 0f
        val selectingPosition: Int
        var selectingProgress: Float
        if (slideToRightSide) {
            selectingPosition = if (isRtl) calculatedPosition - 1 else calculatedPosition + 1
            selectingProgress = positionOffset
        } else {
            selectingPosition = calculatedPosition
            selectingProgress = 1 - positionOffset
        }
        if (selectingProgress > 1) {
            selectingProgress = 1f
        } else if (selectingProgress < 0) {
            selectingProgress = 0f
        }
        return Pair(selectingPosition, selectingProgress)
    }
}