package com.workfort.pstuian.util.view.imageslider.indicatorview.draw.controller

import android.graphics.Canvas
import android.view.MotionEvent
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.data.Value
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.type.IndicatorAnimationType
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Indicator
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.drawer.Drawer
import com.workfort.pstuian.util.view.imageslider.indicatorview.utils.CoordinatesUtils.getPosition
import com.workfort.pstuian.util.view.imageslider.indicatorview.utils.CoordinatesUtils.getXCoordinate
import com.workfort.pstuian.util.view.imageslider.indicatorview.utils.CoordinatesUtils.getYCoordinate

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 7:21.
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

class DrawController(private val indicator: Indicator) {
    private var value: Value? = null
    private val drawer = Drawer(indicator)
    private var listener: ClickListener? = null

    interface ClickListener {
        fun onIndicatorClicked(position: Int)
    }

    fun updateValue(value: Value?) {
        this.value = value
    }

    fun setClickListener(listener: ClickListener?) {
        this.listener = listener
    }

    fun touch(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_UP -> onIndicatorTouched(event.x, event.y)
            else -> {}
        }
    }

    private fun onIndicatorTouched(x: Float, y: Float) {
        if (listener != null) {
            val position = getPosition(indicator, x, y)
            if (position >= 0) {
                listener!!.onIndicatorClicked(position)
            }
        }
    }

    fun draw(canvas: Canvas) {
        val count = indicator.count
        for (position in 0 until count) {
            val coordinateX = getXCoordinate(indicator, position)
            val coordinateY = getYCoordinate(indicator, position)
            drawIndicator(canvas, position, coordinateX, coordinateY)
        }
    }

    private fun drawIndicator(
        canvas: Canvas,
        position: Int,
        coordinateX: Int,
        coordinateY: Int
    ) {
        val interactiveAnimation = indicator.interactiveAnimation
        val selectedPosition = indicator.selectedPosition
        val selectingPosition = indicator.selectingPosition
        val lastSelectedPosition = indicator.lastSelectedPosition
        val selectedItem =
            !interactiveAnimation && (position == selectedPosition || position == lastSelectedPosition)
        val selectingItem =
            interactiveAnimation && (position == selectedPosition || position == selectingPosition)
        val isSelectedItem = selectedItem or selectingItem
        drawer.setup(position, coordinateX, coordinateY)
        if (value != null && isSelectedItem) {
            drawWithAnimation(canvas)
        } else {
            drawer.drawBasic(canvas, isSelectedItem)
        }
    }

    private fun drawWithAnimation(canvas: Canvas) {
        when (indicator.animationType) {
            IndicatorAnimationType.NONE -> drawer.drawBasic(canvas, true)
            IndicatorAnimationType.COLOR -> drawer.drawColor(canvas, value!!)
            IndicatorAnimationType.SCALE -> drawer.drawScale(canvas, value!!)
            IndicatorAnimationType.WORM -> drawer.drawWorm(canvas, value!!)
            IndicatorAnimationType.SLIDE -> drawer.drawSlide(canvas, value!!)
            IndicatorAnimationType.FILL -> drawer.drawFill(canvas, value!!)
            IndicatorAnimationType.THIN_WORM -> drawer.drawThinWorm(canvas, value!!)
            IndicatorAnimationType.DROP -> drawer.drawDrop(canvas, value!!)
            IndicatorAnimationType.SWAP -> drawer.drawSwap(canvas, value!!)
            IndicatorAnimationType.SCALE_DOWN -> drawer.drawScaleDown(canvas, value!!)
            else -> drawer.drawBasic(canvas, true)
        }
    }
}