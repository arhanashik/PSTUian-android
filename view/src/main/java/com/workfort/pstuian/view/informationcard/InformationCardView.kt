package com.workfort.pstuian.view.informationcard

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import com.workfort.pstuian.view.R

/**
 *  ****************************************************************************
 *  * Created by : arhan on 04 Oct, 2021 at 12:21 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class InformationCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    fun setData(title: String?, icon: Drawable?) {
        (findViewById(R.id.tv_title) as TextView).text = HtmlCompat.fromHtml(
            title?: "", HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        (findViewById(R.id.iv_icon) as ImageView).setImageDrawable(icon)
    }

    fun changeBackground(background: Drawable) {
        (findViewById(R.id.container) as ConstraintLayout).background = background
    }
}