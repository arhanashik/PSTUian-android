package com.workfort.pstuian.util.view.imageslider.indicatorview.draw.drawer.type

import android.graphics.Canvas
import android.graphics.Paint
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.Value
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.type.SlideAnimationValue
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Indicator
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Orientation

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 7:53.
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

class SlideDrawer(private val paint: Paint, private val indicator: Indicator) {
    fun draw(
        canvas: Canvas,
        value: Value,
        coordinateX: Int,
        coordinateY: Int
    ) {
        if (value !is SlideAnimationValue) {
            return
        }
        val coordinate = value.coordinate
        val radius = indicator.radius.toFloat()
        paint.color = indicator.unselectedColor
        canvas.drawCircle(coordinateX.toFloat(), coordinateY.toFloat(), radius, paint)
        paint.color = indicator.selectedColor
        if (indicator.orientation === Orientation.HORIZONTAL) {
            canvas.drawCircle(coordinate.toFloat(), coordinateY.toFloat(), radius, paint)
        } else {
            canvas.drawCircle(coordinateX.toFloat(), coordinate.toFloat(), radius, paint)
        }
    }
}