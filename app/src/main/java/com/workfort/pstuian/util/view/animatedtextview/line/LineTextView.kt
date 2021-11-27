package com.workfort.pstuian.util.view.animatedtextview.line

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.workfort.pstuian.util.view.animatedtextview.base.AnimationListener
import com.workfort.pstuian.util.view.animatedtextview.base.AnimatedTextView

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 0:09.
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

class LineTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AnimatedTextView(context, attrs, defStyleAttr) {
    private var lineText: LineText = LineText()

    init {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        lineText = LineText()
        lineText.init(this, attrs, defStyleAttr)
    }

    override fun setAnimationListener(listener: AnimationListener) {
        lineText.setAnimationListener(listener)
    }

    fun setLineColor(color: Int) {
        lineText.setLineColor(color)
    }

    fun getLineWidth(): Float {
        return lineText.getLineWidth()?: return 0f
    }

    fun setLineWidth(lineWidth: Float) {
        lineText.setLineWidth(lineWidth)
    }

    fun getAnimationDuration(): Float {
        return lineText.getAnimationDuration()?: return 0f
    }

    fun setAnimationDuration(animationDuration: Float) {
        lineText.setAnimationDuration(animationDuration)
    }

    override fun setProgress(progress: Float) {
        lineText.setProgress(progress)
    }

    override fun animateText(text: CharSequence) {
        lineText.animateText(text)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        lineText.onDraw(canvas!!)
    }
}