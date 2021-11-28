package com.workfort.pstuian.util.view.animatedtextview.evaporate

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import com.workfort.pstuian.util.view.animatedtextview.base.AnimationListener
import com.workfort.pstuian.util.view.animatedtextview.base.AnimatedTextView

/**
 *  ****************************************************************************
 *  * Created by : arhan on 27 Nov, 2021 at 0:18.
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