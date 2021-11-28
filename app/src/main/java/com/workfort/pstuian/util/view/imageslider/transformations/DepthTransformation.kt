package com.workfort.pstuian.util.view.imageslider.transformations

import android.view.View
import com.workfort.pstuian.util.view.imageslider.SliderPager
import kotlin.math.abs

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 5:58.
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

class DepthTransformation : SliderPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        when {
            position < -1 -> {    // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.alpha = 0f
            }
            position <= 0 -> {    // [-1,0]
                page.alpha = 1f
                page.translationX = 0f
                page.scaleX = 1f
                page.scaleY = 1f
            }
            position <= 1 -> {    // (0,1]
                page.translationX = -position * page.width
                page.alpha = 1 - abs(position)
                page.scaleX = 1 - abs(position)
                page.scaleY = 1 - abs(position)
            }
            else -> {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.alpha = 0f
            }
        }
    }
}