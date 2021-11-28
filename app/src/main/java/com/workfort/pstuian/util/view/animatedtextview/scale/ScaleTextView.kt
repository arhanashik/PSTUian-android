package com.workfort.pstuian.util.view.animatedtextview.scale

import android.content.Context
import android.graphics.Canvas
import android.text.TextUtils
import android.util.AttributeSet
import com.workfort.pstuian.util.view.animatedtextview.base.AnimationListener
import com.workfort.pstuian.util.view.animatedtextview.base.AnimatedTextView

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 1:13.
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