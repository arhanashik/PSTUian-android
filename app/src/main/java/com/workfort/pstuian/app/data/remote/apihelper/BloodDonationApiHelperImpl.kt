package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.blooddonation.BloodDonation
import com.workfort.pstuian.util.remote.BloodDonationApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 1:47 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/1/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class BloodDonationApiHelperImpl(
    private val service: BloodDonationApiService
) : BloodDonationApiHelper() {
    override suspend fun getAll(page: Int, limit: Int): List<BloodDonation> {
        val response = service.getAll(page, limit)
        if(!response.success) throw Exception(response.message)

        return response.data?: throw Exception("No data")
    }

    override suspend fun get(id: Int): BloodDonation {
        val response = service.get(id)
        if(!response.success) throw Exception(response.message)

        return response.data?: throw Exception("No data")
    }

    override suspend fun insert(
        userId: Int,
        userType: String,
        requestId: Int?,
        date: String,
        info: String
    ): BloodDonation {
        val response = service.insert(userId, userType, requestId, date, info)
        if(!response.success) throw Exception(response.message)

        return response.data?: throw Exception("No data")
    }

    override suspend fun update(
        id: Int,
        requestId: Int?,
        date: String,
        info: String
    ): BloodDonation {
        val response = service.update(id, requestId, date, info)
        if(!response.success) throw Exception(response.message)

        return response.data?: throw Exception("No data")
    }

    override suspend fun delete(id: Int): Boolean {
        val response = service.delete(id)
        if(!response.success) throw Exception(response.message)

        return true
    }
}