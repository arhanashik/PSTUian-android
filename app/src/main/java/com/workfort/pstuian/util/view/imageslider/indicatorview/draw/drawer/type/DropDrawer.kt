package com.workfort.pstuian.util.view.imageslider.indicatorview.draw.drawer.type

import android.graphics.Canvas
import android.graphics.Paint
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.Value
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.type.DropAnimationValue
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Indicator
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Orientation

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 7:45.
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

class DropDrawer(private val paint: Paint, private val indicator: Indicator) {
    fun draw(
        canvas: Canvas,
        value: Value,
        coordinateX: Int,
        coordinateY: Int
    ) {
        if (value !is DropAnimationValue) {
            return
        }
        paint.color = indicator.unselectedColor
        canvas.drawCircle(coordinateX.toFloat(), coordinateY.toFloat(), indicator.radius.toFloat(),
            paint)
        paint.color = indicator.selectedColor
        if (indicator.orientation === Orientation.HORIZONTAL) {
            canvas.drawCircle(
                value.width.toFloat(),
                value.height.toFloat(), value.radius.toFloat(), paint)
        } else {
            canvas.drawCircle(
                value.height.toFloat(),
                value.width.toFloat(), value.radius.toFloat(), paint)
        }
    }
}