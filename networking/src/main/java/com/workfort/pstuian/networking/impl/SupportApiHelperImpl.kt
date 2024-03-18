package com.workfort.pstuian.networking.impl

import com.workfort.pstuian.networking.service.SupportApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 28 Oct, 2021 at 11:17 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class SupportApiHelperImpl(private val service: SupportApiService) :
    com.workfort.pstuian.networking.SupportApiHelper {
    override suspend fun sendInquiry(
        name: String,
        email: String,
        type: String,
        query: String
    ): String {
        val response = service.sendInquiry(name, email, type, query)
        if(!response.success) throw Exception(response.message)

        return response.message
    }
}