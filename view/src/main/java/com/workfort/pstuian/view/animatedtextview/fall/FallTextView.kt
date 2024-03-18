package com.workfort.pstuian.view.animatedtextview.fall

import android.content.Context
import android.graphics.Canvas
import android.text.TextUtils
import android.util.AttributeSet
import com.workfort.pstuian.view.animatedtextview.base.AnimatedTextView
import com.workfort.pstuian.view.animatedtextview.base.AnimationListener

class FallTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AnimatedTextView(context, attrs, defStyleAttr) {
    private var fallText: FallText = FallText()

    init {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        fallText = FallText()
        fallText.init(this, attrs, defStyleAttr)
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
    }

    override fun setAnimationListener(listener: AnimationListener) {
        fallText.setAnimationListener(listener)
    }

    override fun setProgress(progress: Float) {
        fallText.setProgress(progress)
    }

    override fun animateText(text: CharSequence) {
        fallText.animateText(text)
    }

    override  fun onDraw(canvas: Canvas) {
        fallText.onDraw(canvas)
    }
}