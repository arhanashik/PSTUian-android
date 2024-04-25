package com.workfort.pstuian.networking.domain

/**
 *  ****************************************************************************
 *  * Created by : arhan on 28 Oct, 2021 at 11:17 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1. The base activity of each activity
 *  * 2. Set layout, toolbar, onClickListeners for the activities
 *  * 3.
 *  ****************************************************************************
 */

interface SupportApiHelper {
    suspend fun sendInquiry(
        name: String,
        email: String,
        type: String,
        query: String
    ): String
}