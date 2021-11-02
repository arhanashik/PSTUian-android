package com.workfort.pstuian.app.ui.base.callback

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 10:45 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 9/30/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

interface BaseLocationCallback {
    fun onComplete(lat: Double, lng: Double)
}