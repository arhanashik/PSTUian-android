package com.workfort.pstuian.view.animatedtextview.line

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.workfort.pstuian.view.animatedtextview.base.AnimatedTextView
import com.workfort.pstuian.view.animatedtextview.base.AnimationListener

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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        lineText.onDraw(canvas)
    }
}