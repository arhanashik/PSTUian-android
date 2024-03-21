package com.workfort.pstuian.view.imageslider.transformations

import android.view.View
import com.workfort.pstuian.view.imageslider.SliderPager
import kotlin.math.abs
import kotlin.math.max

class TossTransformation : SliderPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.translationX = -position * page.width
        page.cameraDistance = 20000f
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
                page.scaleX = max(0.4f, 1 - abs(position))
                page.scaleY = max(0.4f, 1 - abs(position))
                page.rotationX = 1080 * (1 - abs(position) + 1)
                page.translationY = -1000 * abs(position)
            }
            position <= 1 -> {    // (0,1]
                page.alpha = 1f
                page.scaleX = max(0.4f, 1 - abs(position))
                page.scaleY = max(0.4f, 1 - abs(position))
                page.rotationX = -1080 * (1 - abs(position) + 1)
                page.translationY = -1000 * abs(position)
            }
            else -> {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.alpha = 0f
            }
        }
    }
}