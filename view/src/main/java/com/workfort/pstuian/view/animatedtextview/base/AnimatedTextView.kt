package com.workfort.pstuian.view.animatedtextview.base

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 *  ****************************************************************************
 *  * Created by : arhan on 26 Nov, 2021 at 23:54.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

abstract class AnimatedTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    abstract fun setAnimationListener(listener: AnimationListener)
    abstract fun setProgress(progress: Float)
    abstract fun animateText(text: CharSequence)
}