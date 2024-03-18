package com.workfort.pstuian.view.imageslider.indicatorview.draw.drawer.type

import android.graphics.Canvas
import android.graphics.Paint
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.Value
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.type.ScaleAnimationValue
import com.workfort.pstuian.view.imageslider.indicatorview.draw.data.Indicator

class ScaleDrawer(private val paint: Paint, private val indicator: Indicator) {
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