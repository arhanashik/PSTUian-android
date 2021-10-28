package com.workfort.pstuian.util.lib.fcm.callback

/**
 *  ****************************************************************************
 *  * Created by : arhan on 28 Oct, 2021 at 13:26.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/10/28.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

interface FcmTokenCallback {
    fun onResponse(token: String? = null, error: String? = null)
}