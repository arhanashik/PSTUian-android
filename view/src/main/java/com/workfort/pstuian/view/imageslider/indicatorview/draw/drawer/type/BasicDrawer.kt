package com.workfort.pstuian.view.imageslider.indicatorview.draw.drawer.type

import android.graphics.Canvas
import android.graphics.Paint
import com.workfort.pstuian.view.imageslider.indicatorview.animation.type.IndicatorAnimationType
import com.workfort.pstuian.view.imageslider.indicatorview.draw.data.Indicator

class BasicDrawer(private val paint: Paint, private val indicator: Indicator) {
    private val strokePaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = indicator.stroke.toFloat()
    }

    fun draw(
        canvas: Canvas,
        position: Int,
        isSelectedItem: Boolean,
        coordinateX: Int,
        coordinateY: Int
    ) {
        var radius: Float = indicator.radius.toFloat()
        val strokePx: Int = indicator.stroke
        val scaleFactor: Float = indicator.scaleFactor
        val selectedColor: Int = indicator.selectedColor
        val unselectedColor: Int = indicator.unselectedColor
        val selectedPosition: Int = indicator.selectedPosition
        val animationType: IndicatorAnimationType? = indicator.animationType
        if (animationType == IndicatorAnimationType.SCALE && !isSelectedItem) {
            radius *= scaleFactor
        } else if (animationType == IndicatorAnimationType.SCALE_DOWN && isSelectedItem) {
            radius *= scaleFactor
        }
        var color = unselectedColor
        if (position == selectedPosition) {
            color = selectedColor
        }
        val paint: Paint
        if (animationType == IndicatorAnimationType.FILL && position != selectedPosition) {
            paint = strokePaint
            paint.strokeWidth = strokePx.toFloat()
        } else {
            paint = this.paint
        }
        paint.color = color
        canvas.drawCircle(coordinateX.toFloat(), coordinateY.toFloat(), radius, paint)
    }
}