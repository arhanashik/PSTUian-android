package com.workfort.pstuian.view.imageslider.indicatorview.draw

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Pair
import android.view.MotionEvent
import com.workfort.pstuian.view.imageslider.indicatorview.animation.data.Value
import com.workfort.pstuian.view.imageslider.indicatorview.draw.controller.AttributeController
import com.workfort.pstuian.view.imageslider.indicatorview.draw.controller.DrawController
import com.workfort.pstuian.view.imageslider.indicatorview.draw.controller.MeasureController
import com.workfort.pstuian.view.imageslider.indicatorview.draw.data.Indicator

class DrawManager {
    var indicator = Indicator()
    private val drawController = DrawController(indicator)
    private val attributeController = AttributeController(indicator)

    fun setClickListener(listener: DrawController.ClickListener?) {
        drawController.setClickListener(listener)
    }

    fun touch(event: MotionEvent) {
        drawController.touch(event)
    }

    fun updateValue(value: Value?) {
        drawController.updateValue(value)
    }

    fun draw(canvas: Canvas) {
        drawController.draw(canvas)
    }

    fun measureViewSize(widthMeasureSpec: Int, heightMeasureSpec: Int): Pair<Int, Int> {
        return MeasureController.measureViewSize(indicator, widthMeasureSpec, heightMeasureSpec)
    }

    fun initAttributes(context: Context, attrs: AttributeSet?) {
        attributeController.init(context, attrs)
    }
}