package com.workfort.pstuian.util.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import com.workfort.pstuian.R
import timber.log.Timber


/**
 *  ****************************************************************************
 *  * Created by : arhan on 04 Oct, 2021 at 12:21 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/4/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */
class InformationCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    fun setData(title: String?, icon: Drawable?) {
        findViewById<TextView>(R.id.tv_title).text = HtmlCompat.fromHtml(title?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
        findViewById<ImageView>(R.id.iv_icon).setImageDrawable(icon)
    }

    fun changeBackground(background: Drawable) {
        findViewById<ConstraintLayout>(R.id.container).background = background
    }
}