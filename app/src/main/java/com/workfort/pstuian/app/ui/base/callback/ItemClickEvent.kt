package com.workfort.pstuian.app.ui.base.callback

import com.workfort.pstuian.app.data.local.batch.BatchEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 29 Oct, 2021 at 21:22.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/10/29.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

interface ItemClickEvent<T> {
    fun onClickItem(item: T)
}