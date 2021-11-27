package com.workfort.pstuian.util.view.imageslider.indicatorview.draw.controller

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.workfort.pstuian.R
import com.workfort.pstuian.util.view.imageslider.indicatorview.animation.type.*
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Indicator
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.Orientation
import com.workfort.pstuian.util.view.imageslider.indicatorview.draw.data.RtlMode
import com.workfort.pstuian.util.view.imageslider.indicatorview.utils.DensityUtils.dpToPx

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 7:24.
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

class AttributeController(private val indicator: Indicator) {
    fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PageIndicatorView,
            0, 0)
        initCountAttribute(typedArray)
        initColorAttribute(typedArray)
        initAnimationAttribute(typedArray)
        initSizeAttribute(typedArray)
        typedArray.recycle()
    }

    private fun initCountAttribute(typedArray: TypedArray) {
        val viewPagerId =
            typedArray.getResourceId(R.styleable.PageIndicatorView_piv_viewPager, View.NO_ID)
        val autoVisibility =
            typedArray.getBoolean(R.styleable.PageIndicatorView_piv_autoVisibility, true)
        val dynamicCount =
            typedArray.getBoolean(R.styleable.PageIndicatorView_piv_dynamicCount, false)
        var count = typedArray.getInt(R.styleable.PageIndicatorView_piv_count, Indicator.COUNT_NONE)
        if (count == Indicator.COUNT_NONE) {
            count = Indicator.DEFAULT_COUNT
        }
        var position = typedArray.getInt(R.styleable.PageIndicatorView_piv_select, 0)
        if (position < 0) {
            position = 0
        } else if (count > 0 && position > count - 1) {
            position = count - 1
        }
        indicator.viewPagerId = viewPagerId
        indicator.autoVisibility = autoVisibility
        indicator.dynamicCount = dynamicCount
        indicator.count = count
        indicator.selectedPosition = position
        indicator.selectingPosition = position
        indicator.lastSelectedPosition = position
    }

    private fun initColorAttribute(typedArray: TypedArray) {
        val unselectedColor = typedArray.getColor(
            R.styleable.PageIndicatorView_piv_unselectedColor,
            Color.parseColor(ColorAnimation.DEFAULT_UNSELECTED_COLOR)
        )
        val selectedColor = typedArray.getColor(
            R.styleable.PageIndicatorView_piv_selectedColor,
            Color.parseColor(ColorAnimation.DEFAULT_SELECTED_COLOR)
        )
        indicator.unselectedColor = unselectedColor
        indicator.selectedColor = selectedColor
    }

    private fun initAnimationAttribute(typedArray: TypedArray) {
        val interactiveAnimation = typedArray.getBoolean(
            R.styleable.PageIndicatorView_piv_interactiveAnimation, false)
        var animationDuration = typedArray.getInt(
            R.styleable.PageIndicatorView_piv_animationDuration,
            BaseAnimation.DEFAULT_ANIMATION_TIME
        )
        if (animationDuration < 0) {
            animationDuration = 0
        }
        val animIndex = typedArray.getInt(
            R.styleable.PageIndicatorView_piv_animationType,
            IndicatorAnimationType.NONE.ordinal
        )
        val animationType = getAnimationType(animIndex)
        val rtlIndex = typedArray.getInt(R.styleable.PageIndicatorView_piv_rtl_mode,
            RtlMode.Off.ordinal)
        val rtlMode = getRtlMode(rtlIndex)
        indicator.animationDuration = animationDuration.toLong()
        indicator.interactiveAnimation = interactiveAnimation
        indicator.animationType = animationType
        indicator.rtlMode = rtlMode
    }

    private fun initSizeAttribute(typedArray: TypedArray) {
        val orientationIndex = typedArray.getInt(
            R.styleable.PageIndicatorView_piv_orientation,
            Orientation.HORIZONTAL.ordinal
        )
        val orientation: Orientation = if (orientationIndex == 0) {
            Orientation.HORIZONTAL
        } else {
            Orientation.VERTICAL
        }
        var radius =
            typedArray.getDimension(
                R.styleable.PageIndicatorView_piv_radius,
                dpToPx(Indicator.DEFAULT_RADIUS_DP).toFloat()
            )
                .toInt()
        if (radius < 0) {
            radius = 0
        }
        var padding =
            typedArray.getDimension(
                R.styleable.PageIndicatorView_piv_padding,
                dpToPx(Indicator.DEFAULT_PADDING_DP).toFloat()
            )
                .toInt()
        if (padding < 0) {
            padding = 0
        }
        var scaleFactor = typedArray.getFloat(
            R.styleable.PageIndicatorView_piv_scaleFactor,
            ScaleAnimation.DEFAULT_SCALE_FACTOR
        )
        if (scaleFactor < ScaleAnimation.MIN_SCALE_FACTOR) {
            scaleFactor = ScaleAnimation.MIN_SCALE_FACTOR
        } else if (scaleFactor > ScaleAnimation.MAX_SCALE_FACTOR) {
            scaleFactor = ScaleAnimation.MAX_SCALE_FACTOR
        }
        var stroke =
            typedArray.getDimension(
                R.styleable.PageIndicatorView_piv_strokeWidth,
                dpToPx(FillAnimation.DEFAULT_STROKE_DP).toFloat()
            )
                .toInt()
        if (stroke > radius) {
            stroke = radius
        }
        if (indicator.animationType != IndicatorAnimationType.FILL) {
            stroke = 0
        }
        indicator.radius = radius
        indicator.orientation = orientation
        indicator.padding = padding
        indicator.scaleFactor = scaleFactor
        indicator.stroke = stroke
    }

    private fun getAnimationType(index: Int): IndicatorAnimationType {
        when (index) {
            0 -> return IndicatorAnimationType.NONE
            1 -> return IndicatorAnimationType.COLOR
            2 -> return IndicatorAnimationType.SCALE
            3 -> return IndicatorAnimationType.WORM
            4 -> return IndicatorAnimationType.SLIDE
            5 -> return IndicatorAnimationType.FILL
            6 -> return IndicatorAnimationType.THIN_WORM
            7 -> return IndicatorAnimationType.DROP
            8 -> return IndicatorAnimationType.SWAP
            9 -> return IndicatorAnimationType.SCALE_DOWN
        }
        return IndicatorAnimationType.NONE
    }

    companion object {
        fun getRtlMode(index: Int): RtlMode {
            return when (index) {
                0 -> RtlMode.On
                1 -> RtlMode.Off
                else -> RtlMode.Auto
            }
        }
    }
}