package com.workfort.pstuian.repository

import com.workfort.pstuian.networking.SupportApiHelper

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 4:03 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class SupportRepository(private val helper: com.workfort.pstuian.networking.SupportApiHelper) {
    suspend fun sendInquiry(
        name: String,
        email: String,
        type: String,
        query: String
    ): String {
        return helper.sendInquiry(name, email, type, query)
    }
}