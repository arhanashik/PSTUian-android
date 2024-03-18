package com.workfort.pstuian.view.animatedtextview.scale

import android.content.Context
import android.graphics.Canvas
import android.text.TextUtils
import android.util.AttributeSet
import com.workfort.pstuian.view.animatedtextview.base.AnimatedTextView
import com.workfort.pstuian.view.animatedtextview.base.AnimationListener

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 1:13.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class ScaleTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AnimatedTextView(context, attrs, defStyleAttr) {
    private var scaleText: ScaleText = ScaleText()

    init {
        scaleText = ScaleText()
        scaleText.init(this, attrs, defStyleAttr)
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
    }

    override fun setAnimationListener(listener: AnimationListener) {
        scaleText.setAnimationListener(listener)
    }

    override fun onDraw(canvas: Canvas) {
        scaleText.onDraw(canvas)
    }

    override fun setProgress(progress: Float) {
        scaleText.setProgress(progress)
    }

    override fun animateText(text: CharSequence) {
        scaleText.animateText(text)
    }
}