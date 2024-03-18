package com.workfort.pstuian.view.animatedtextview.evaporate

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import com.workfort.pstuian.view.animatedtextview.base.AnimatedTextView
import com.workfort.pstuian.view.animatedtextview.base.AnimationListener

class EvaporateTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AnimatedTextView(context, attrs, defStyleAttr) {
    private var evaporateText: EvaporateText? = null

    init {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        evaporateText = EvaporateText()
        evaporateText?.init(this, attrs, defStyleAttr)
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
    }

    override fun setAnimationListener(listener: AnimationListener) {
        evaporateText?.setAnimationListener(listener)
    }

    override fun setProgress(progress: Float) {
        evaporateText?.setProgress(progress)
    }

    override fun animateText(text: CharSequence) {
        evaporateText?.animateText(text)
    }
}