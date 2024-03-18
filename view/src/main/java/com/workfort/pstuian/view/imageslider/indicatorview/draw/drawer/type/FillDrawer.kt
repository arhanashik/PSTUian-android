package com.workfort.pstuian.view.imageslider.indicatorview.draw.drawer.type

import android.graphics.Canvas
import android.graphics.Paint
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.Value
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.type.FillAnimationValue
import com.workfort.pstuian.view.imageslider.indicatorview.draw.data.Indicator

class FillDrawer(private val paint: Paint, private val indicator: Indicator) {
    private val strokePaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
    }
    fun draw(
        canvas: Canvas,
        value: Value,
        position: Int,
        coordinateX: Int,
        coordinateY: Int
    ) {
        if (value !is FillAnimationValue) {
            return
        }
        var color: Int = indicator.unselectedColor
        var radius: Float = indicator.radius.toFloat()
        var stroke: Int = indicator.stroke
        val selectedPosition: Int = indicator.selectedPosition
        val selectingPosition: Int = indicator.selectingPosition
        val lastSelectedPosition: Int = indicator.lastSelectedPosition
        if (indicator.interactiveAnimation) {
            if (position == selectingPosition) {
                color = value.color
                radius = value.radius.toFloat()
                stroke = value.stroke
            } else if (position == selectedPosition) {
                color = value.colorReverse
                radius = value.radiusReverse.toFloat()
                stroke = value.strokeReverse
            }
        } else {
            if (position == selectedPosition) {
                color = value.color
                radius = value.radius.toFloat()
                stroke = value.stroke
            } else if (position == lastSelectedPosition) {
                color = value.colorReverse
                radius = value.radiusReverse.toFloat()
                stroke = value.strokeReverse
            }
        }
        strokePaint.color = color
        strokePaint.strokeWidth = indicator.stroke.toFloat()
        canvas.drawCircle(
            coordinateX.toFloat(),
            coordinateY.toFloat(),
            indicator.radius.toFloat(),
            strokePaint
        )
        strokePaint.strokeWidth = stroke.toFloat()
        canvas.drawCircle(coordinateX.toFloat(), coordinateY.toFloat(), radius, strokePaint)
    }
}