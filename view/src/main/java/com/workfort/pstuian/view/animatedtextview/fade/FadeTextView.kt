package com.workfort.pstuian.view.animatedtextview.fade

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.workfort.pstuian.view.animatedtextview.base.AnimatedTextView
import com.workfort.pstuian.view.animatedtextview.base.AnimationListener

class FadeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AnimatedTextView(context, attrs, defStyleAttr) {
    private lateinit var fadeText: FadeText

    init {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        fadeText = FadeText()
        fadeText.init(this, attrs, defStyleAttr)
    }

    override fun setAnimationListener(listener: AnimationListener) {
        fadeText.setAnimationListener(listener)
    }

    fun getAnimationDuration(): Int {
        return fadeText.getAnimationDuration()
    }

    fun setAnimationDuration(animationDuration: Int) {
        fadeText.setAnimationDuration(animationDuration)
    }

    override fun setProgress(progress: Float) {
        fadeText.setProgress(progress)
    }

    override fun animateText(text: CharSequence) {
        fadeText.animateText(text)
    }

    override fun onDraw(canvas: Canvas) {
        fadeText.onDraw(canvas)
    }
}