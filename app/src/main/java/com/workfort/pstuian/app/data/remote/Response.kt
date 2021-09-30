package com.workfort.pstuian.app.data.remote

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 9:57 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1. The base activity of each activity
 *  * 2. Set layout, toolbar, onClickListeners for the activities
 *  * 3.
 *  *
 *  * Last edited by : arhan on 9/30/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

data class Response<T> (
    var success: Boolean,
    var message: String,
    var data: T? = null
)