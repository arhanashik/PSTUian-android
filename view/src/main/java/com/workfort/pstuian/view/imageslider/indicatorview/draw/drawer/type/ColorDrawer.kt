package com.workfort.pstuian.view.imageslider.indicatorview.draw.drawer.type

import android.graphics.Canvas
import android.graphics.Paint
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.Value
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.type.ColorAnimationValue
import com.workfort.pstuian.view.imageslider.indicatorview.draw.data.Indicator

class ColorDrawer(private val paint: Paint, private val indicator: Indicator) {
    fun draw(
        canvas: Canvas,
        value: Value,
        position: Int,
        coordinateX: Int,
        coordinateY: Int
    ) {
        if (value !is ColorAnimationValue) {
            return
        }
        val radius: Int = indicator.radius
        var color: Int = indicator.selectedColor
        val selectedPosition: Int = indicator.selectedPosition
        val selectingPosition: Int = indicator.selectingPosition
        val lastSelectedPosition: Int = indicator.lastSelectedPosition
        if (indicator.interactiveAnimation) {
            if (position == selectingPosition) {
                color = value.color
            } else if (position == selectedPosition) {
                color = value.colorReverse
            }
        } else {
            if (position == selectedPosition) {
                color = value.color
            } else if (position == lastSelectedPosition) {
                color = value.colorReverse
            }
        }
        paint.color = color
        canvas.drawCircle(coordinateX.toFloat(), coordinateY.toFloat(), radius.toFloat(), paint)
    }
}