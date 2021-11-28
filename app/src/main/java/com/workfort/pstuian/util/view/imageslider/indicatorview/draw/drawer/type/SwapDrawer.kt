package com.workfort.pstuian.util.view.imageslider.indicatorview.draw.drawer.type

import android.graphics.Canvas
import android.graphics.Paint
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.Value
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.type.SwapAnimationValue
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Indicator
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Orientation

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 7:55.
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

class SwapDrawer(private val paint: Paint, private val indicator: Indicator) {
    fun draw(
        canvas: Canvas,
        value: Value,
        position: Int,
        coordinateX: Int,
        coordinateY: Int
    ) {
        if (value !is SwapAnimationValue) {
            return
        }
        val selectedColor: Int = indicator.selectedColor
        val unselectedColor: Int = indicator.unselectedColor
        val radius = indicator.radius.toFloat()
        val selectedPosition: Int = indicator.selectedPosition
        val selectingPosition: Int = indicator.selectingPosition
        val lastSelectedPosition: Int = indicator.lastSelectedPosition
        var coordinate = value.coordinate
        var color = unselectedColor
        if (indicator.interactiveAnimation) {
            if (position == selectingPosition) {
                coordinate = value.coordinate
                color = selectedColor
            } else if (position == selectedPosition) {
                coordinate = value.coordinateReverse
                color = unselectedColor
            }
        } else {
            if (position == lastSelectedPosition) {
                coordinate = value.coordinate
                color = selectedColor
            } else if (position == selectedPosition) {
                coordinate = value.coordinateReverse
                color = unselectedColor
            }
        }
        paint.color = color
        if (indicator.orientation === Orientation.HORIZONTAL) {
            canvas.drawCircle(coordinate.toFloat(), coordinateY.toFloat(), radius, paint)
        } else {
            canvas.drawCircle(coordinateX.toFloat(), coordinate.toFloat(), radius, paint)
        }
    }
}