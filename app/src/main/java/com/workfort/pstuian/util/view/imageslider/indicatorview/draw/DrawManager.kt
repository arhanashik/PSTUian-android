package com.workfort.pstuian.util.view.imageslider.indicatorview.draw

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Pair
import android.view.MotionEvent
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.Value
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.controller.AttributeController
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.controller.DrawController
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.controller.DrawController.ClickListener
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.controller.MeasureController
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Indicator

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 6:31.
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

class DrawManager {
    var indicator = Indicator()
    private val drawController = DrawController(indicator)
    private val attributeController = AttributeController(indicator)

    fun setClickListener(listener: ClickListener?) {
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