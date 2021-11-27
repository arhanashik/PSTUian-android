package com.workfort.pstuian.util.view.animatedtextview.base

import android.graphics.Canvas
import android.util.AttributeSet

interface IAnimatedText {
    fun init(animatedTextView: AnimatedTextView, attrs: AttributeSet?, defStyle: Int)
    fun animateText(text: CharSequence)
    fun onDraw(canvas: Canvas)
    fun setAnimationListener(listener: AnimationListener)
}