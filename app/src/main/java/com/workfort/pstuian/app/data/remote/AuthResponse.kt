package com.workfort.pstuian.app.data.remote

import com.google.gson.annotations.SerializedName

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 12:22 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1. The base activity of each activity
 *  * 2. Set layout, toolbar, onClickListeners for the activities
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/14/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

data class AuthResponse<T> (
    var success: Boolean,
    var code: Int? = -1,
    var message: String,
    var data: T? = null,
    @SerializedName("auth_token")
    var authToken: String? = null
)