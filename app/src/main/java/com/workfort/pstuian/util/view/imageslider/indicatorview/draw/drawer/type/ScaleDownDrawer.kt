package com.workfort.pstuian.util.view.imageslider.indicatorview.draw.drawer.type

import android.graphics.Canvas
import android.graphics.Paint
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.Value
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.type.ScaleAnimationValue
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Indicator

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 7:50.
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

class ScaleDownDrawer(private val paint: Paint, private val indicator: Indicator) {
    fun draw(
        canvas: Canvas,
        value: Value,
        position: Int,
        coordinateX: Int,
        coordinateY: Int
    ) {
        if (value !is ScaleAnimationValue) {
            return
        }
        var radius: Float = indicator.radius.toFloat()
        var color: Int = indicator.selectedColor
        val selectedPosition: Int = indicator.selectedPosition
        val selectingPosition: Int = indicator.selectingPosition
        val lastSelectedPosition: Int = indicator.lastSelectedPosition
        if (indicator.interactiveAnimation) {
            if (position == selectingPosition) {
                radius = value.radius.toFloat()
                color = value.color
            } else if (position == selectedPosition) {
                radius = value.radiusReverse.toFloat()
                color = value.colorReverse
            }
        } else {
            if (position == selectedPosition) {
                radius = value.radius.toFloat()
                color = value.color
            } else if (position == lastSelectedPosition) {
                radius = value.radiusReverse.toFloat()
                color = value.colorReverse
            }
        }
        paint.color = color
        canvas.drawCircle(coordinateX.toFloat(), coordinateY.toFloat(), radius, paint)
    }
}