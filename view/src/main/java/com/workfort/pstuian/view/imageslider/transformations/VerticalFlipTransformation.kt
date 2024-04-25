package com.workfort.pstuian.view.imageslider.transformations

import android.view.View
import com.workfort.pstuian.view.imageslider.SliderPager
import kotlin.math.abs

class VerticalFlipTransformation : SliderPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.translationX = -position * page.width
        page.cameraDistance = 12000f
        if (position < 0.5 && position > -0.5) {
            page.visibility = View.VISIBLE
        } else {
            page.visibility = View.INVISIBLE
        }
        when {
            position < -1 -> {     // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.alpha = 0f
            }
            position <= 0 -> {    // [-1,0]
                page.alpha = 1f
                page.rotationY = 180 * (1 - abs(position) + 1)
            }
            position <= 1 -> {    // (0,1]
                page.alpha = 1f
                page.rotationY = -180 * (1 - abs(position) + 1)
            }
            else -> {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.alpha = 0f
            }
        }
    }
}