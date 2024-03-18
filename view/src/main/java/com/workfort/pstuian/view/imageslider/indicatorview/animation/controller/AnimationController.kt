package com.workfort.pstuian.view.imageslider.indicatorview.animation.controller

import com.workfort.pstuian.view.imageslider.indicatorview.animation.type.BaseAnimation
import com.workfort.pstuian.view.imageslider.indicatorview.animation.type.IndicatorAnimationType
import com.workfort.pstuian.view.imageslider.indicatorview.draw.data.Indicator
import com.workfort.pstuian.view.imageslider.indicatorview.draw.data.Orientation
import com.workfort.pstuian.view.imageslider.indicatorview.utils.CoordinatesUtils.getCoordinate

class AnimationController(
    private val indicator: Indicator,
    private val listener: ValueController.UpdateListener
) {
    private val valueController = ValueController(listener)
    private var runningAnimation: BaseAnimation<*>? = null
    private var progress = 0f
    private var isInteractive = false

    fun interactive(progress: Float) {
        isInteractive = true
        this.progress = progress
        animate()
    }

    fun basic() {
        isInteractive = false
        progress = 0f
        animate()
    }

    fun end() {
        runningAnimation?.end()
    }

    private fun animate() {
        when (indicator.animationType) {
            IndicatorAnimationType.NONE -> listener.onValueUpdated(null)
            IndicatorAnimationType.COLOR -> colorAnimation()
            IndicatorAnimationType.SCALE -> scaleAnimation()
            IndicatorAnimationType.WORM -> wormAnimation()
            IndicatorAnimationType.FILL -> fillAnimation()
            IndicatorAnimationType.SLIDE -> slideAnimation()
            IndicatorAnimationType.THIN_WORM -> thinWormAnimation()
            IndicatorAnimationType.DROP -> dropAnimation()
            IndicatorAnimationType.SWAP -> swapAnimation()
            IndicatorAnimationType.SCALE_DOWN -> scaleDownAnimation()
            else -> listener.onValueUpdated(null)
        }
    }

    private fun colorAnimation() {
        val selectedColor = indicator.selectedColor
        val unselectedColor = indicator.unselectedColor
        val animationDuration = indicator.animationDuration
        val animation = valueController
            .color()
            .with(unselectedColor, selectedColor)
            .duration(animationDuration)
        if (isInteractive) {
            animation.progress(progress)
        } else {
            animation.start()
        }
        runningAnimation = animation
    }

    private fun scaleAnimation() {
        val selectedColor = indicator.selectedColor
        val unselectedColor = indicator.unselectedColor
        val radiusPx = indicator.radius
        val scaleFactor = indicator.scaleFactor
        val animationDuration = indicator.animationDuration
        val animation = valueController
            .scale()
            .with(unselectedColor, selectedColor, radiusPx, scaleFactor)
            .duration(animationDuration)
        if (isInteractive) {
            animation.progress(progress)
        } else {
            animation.start()
        }
        runningAnimation = animation
    }

    private fun wormAnimation() {
        val fromPosition =
            if (indicator.interactiveAnimation) indicator.selectedPosition else indicator.lastSelectedPosition
        val toPosition =
            if (indicator.interactiveAnimation) indicator.selectingPosition else indicator.selectedPosition
        val from = getCoordinate(indicator, fromPosition)
        val to = getCoordinate(indicator, toPosition)
        val isRightSide = toPosition > fromPosition
        val radiusPx = indicator.radius
        val animationDuration = indicator.animationDuration
        val animation: BaseAnimation<*> = valueController
            .worm()
            .with(from, to, radiusPx, isRightSide)
            .duration(animationDuration)
        if (isInteractive) {
            animation.progress(progress)
        } else {
            animation.start()
        }
        runningAnimation = animation
    }

    private fun slideAnimation() {
        val fromPosition =
            if (indicator.interactiveAnimation) indicator.selectedPosition else indicator.lastSelectedPosition
        val toPosition =
            if (indicator.interactiveAnimation) indicator.selectingPosition else indicator.selectedPosition
        val from = getCoordinate(indicator, fromPosition)
        val to = getCoordinate(indicator, toPosition)
        val animationDuration = indicator.animationDuration
        val animation = valueController
            .slide()
            .with(from, to)
            .duration(animationDuration)
        if (isInteractive) {
            animation.progress(progress)
        } else {
            animation.start()
        }
        runningAnimation = animation
    }

    private fun fillAnimation() {
        val selectedColor = indicator.selectedColor
        val unselectedColor = indicator.unselectedColor
        val radiusPx = indicator.radius
        val strokePx = indicator.stroke
        val animationDuration = indicator.animationDuration
        val animation = valueController
            .fill()
            .with(unselectedColor, selectedColor, radiusPx, strokePx)
            .duration(animationDuration)
        if (isInteractive) {
            animation.progress(progress)
        } else {
            animation.start()
        }
        runningAnimation = animation
    }

    private fun thinWormAnimation() {
        val fromPosition =
            if (indicator.interactiveAnimation) indicator.selectedPosition else indicator.lastSelectedPosition
        val toPosition =
            if (indicator.interactiveAnimation) indicator.selectingPosition else indicator.selectedPosition
        val from = getCoordinate(indicator, fromPosition)
        val to = getCoordinate(indicator, toPosition)
        val isRightSide = toPosition > fromPosition
        val radiusPx = indicator.radius
        val animationDuration = indicator.animationDuration
        val animation: BaseAnimation<*> = valueController
            .thinWorm()
            .with(from, to, radiusPx, isRightSide)
            .duration(animationDuration)
        if (isInteractive) {
            animation.progress(progress)
        } else {
            animation.start()
        }
        runningAnimation = animation
    }

    private fun dropAnimation() {
        val fromPosition =
            if (indicator.interactiveAnimation) indicator.selectedPosition else indicator.lastSelectedPosition
        val toPosition =
            if (indicator.interactiveAnimation) indicator.selectingPosition else indicator.selectedPosition
        val widthFrom = getCoordinate(indicator, fromPosition)
        val widthTo = getCoordinate(indicator, toPosition)
        val paddingTop = indicator.paddingTop
        val paddingLeft = indicator.paddingLeft
        val padding =
            if (indicator.orientation === Orientation.HORIZONTAL) paddingTop else paddingLeft
        val radius = indicator.radius
        val heightFrom = radius * 3 + padding
        val heightTo = radius + padding
        val animationDuration = indicator.animationDuration
        val animation: BaseAnimation<*> = valueController
            .drop()
            .duration(animationDuration)
            .with(widthFrom, widthTo, heightFrom, heightTo, radius)
        if (isInteractive) {
            animation.progress(progress)
        } else {
            animation.start()
        }
        runningAnimation = animation
    }

    private fun swapAnimation() {
        val fromPosition =
            if (indicator.interactiveAnimation) indicator.selectedPosition else indicator.lastSelectedPosition
        val toPosition =
            if (indicator.interactiveAnimation) indicator.selectingPosition else indicator.selectedPosition
        val from = getCoordinate(indicator, fromPosition)
        val to = getCoordinate(indicator, toPosition)
        val animationDuration = indicator.animationDuration
        val animation = valueController
            .swap()
            .with(from, to)
            .duration(animationDuration)
        if (isInteractive) {
            animation.progress(progress)
        } else {
            animation.start()
        }
        runningAnimation = animation
    }

    private fun scaleDownAnimation() {
        val selectedColor = indicator.selectedColor
        val unselectedColor = indicator.unselectedColor
        val radiusPx = indicator.radius
        val scaleFactor = indicator.scaleFactor
        val animationDuration = indicator.animationDuration
        val animation = valueController
            .scaleDown()
            .with(unselectedColor, selectedColor, radiusPx, scaleFactor)
            .duration(animationDuration)
        if (isInteractive) {
            animation.progress(progress)
        } else {
            animation.start()
        }
        runningAnimation = animation
    }
}