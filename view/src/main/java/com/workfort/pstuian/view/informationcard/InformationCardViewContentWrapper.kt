package com.workfort.pstuian.view.informationcard

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.google.android.material.card.MaterialCardView
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

class InformationCardViewContentWrapper @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private var mTitle: String? = ""
    private var mIcon: Drawable? = null
    private var mBackground: Drawable? = null
    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.InformationCardViewContentWrapper,
            0, 0
        )

        mTitle = a.getString(R.styleable.InformationCardViewContentWrapper_title)
        mIcon = a.getDrawable(R.styleable.InformationCardViewContentWrapper_icon)
        mBackground = a.getDrawable(R.styleable.InformationCardViewContentWrapper_background)
        a.recycle()
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        if (child is InformationCardView) {
            child.setData(mTitle, mIcon)
            mBackground?.let {
                child.changeBackground(it)
            }
        }
    }
}