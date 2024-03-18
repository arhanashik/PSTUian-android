package com.workfort.pstuian.view.imageslider.transformations

import android.view.View
import com.workfort.pstuian.view.imageslider.SliderPager
import kotlin.math.abs

class GateTransformation : SliderPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.translationX = -position * page.width
        when {
            position < -1 -> {    // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.alpha = 0f
            }
            position <= 0 -> {    // [-1,0]
                page.alpha = 1f
                page.pivotX = 0f
                page.rotationY = 90 * abs(position)
            }
            position <= 1 -> {    // (0,1]
                page.alpha = 1f
                page.pivotX = page.width.toFloat()
                page.rotationY = -90 * abs(position)
            }
            else -> {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.alpha = 0f
            }
        }
    }
}