package com.workfort.pstuian.util.view.imageslider.transformations

import android.view.View
import com.workfort.pstuian.util.view.imageslider.SliderPager
import kotlin.math.abs
import kotlin.math.max

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 5:56.
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

class CubeOutDepthTransformation : SliderPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        when {
            position < -1 -> {    // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.alpha = 0f
            }
            position <= 0 -> {    // [-1,0]
                page.alpha = 1f
                page.pivotX = page.width.toFloat()
                page.rotationY = -90 * abs(position)
            }
            position <= 1 -> {    // (0,1]
                page.alpha = 1f
                page.pivotX = 0f
                page.rotationY = 90 * abs(position)
            }
            else -> {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.alpha = 0f
            }
        }
        if (abs(position) <= 0.5) {
            page.scaleY = max(0.4f, 1 - abs(position))
        } else if (abs(position) <= 1) {
            page.scaleY = max(0.4f, 1 - abs(position))
        }
    }
}