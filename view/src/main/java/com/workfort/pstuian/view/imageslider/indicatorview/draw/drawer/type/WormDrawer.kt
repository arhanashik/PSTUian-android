package com.workfort.pstuian.view.imageslider.indicatorview.draw.drawer.type

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.Value
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.type.WormAnimationValue
import com.workfort.pstuian.view.imageslider.indicatorview.draw.data.Indicator
import com.workfort.pstuian.view.imageslider.indicatorview.draw.data.Orientation

open class WormDrawer(private val paint: Paint, private val indicator: Indicator) {
    var rect = RectF()
    open fun draw(
        canvas: Canvas,
        value: Value,
        coordinateX: Int,
        coordinateY: Int
    ) {
        if (value !is WormAnimationValue) {
            return
        }
        val rectStart = value.rectStart
        val rectEnd = value.rectEnd
        val radius = indicator.radius.toFloat()
        val unselectedColor: Int = indicator.unselectedColor
        val selectedColor: Int = indicator.selectedColor
        if (indicator.orientation === Orientation.HORIZONTAL) {
            rect.left = rectStart.toFloat()
            rect.right = rectEnd.toFloat()
            rect.top = coordinateY - radius
            rect.bottom = coordinateY + radius
        } else {
            rect.left = coordinateX - radius
            rect.right = coordinateX + radius
            rect.top = rectStart.toFloat()
            rect.bottom = rectEnd.toFloat()
        }
        paint.color = unselectedColor
        canvas.drawCircle(coordinateX.toFloat(), coordinateY.toFloat(), radius, paint)
        paint.color = selectedColor
        canvas.drawRoundRect(rect, radius, radius, paint)
    }
}