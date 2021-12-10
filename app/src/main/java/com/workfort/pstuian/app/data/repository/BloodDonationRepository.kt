package com.workfort.pstuian.app.data.repository

import com.workfort.pstuian.app.data.remote.apihelper.BloodDonationApiHelper

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 1:50 AM.
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

class BloodDonationRepository(private val helper: BloodDonationApiHelper) {
    suspend fun getAll(page: Int) = helper.getAll(page, limit = 20)
    suspend fun get(id: Int) = helper.get(id)
    suspend fun insert(
        userId: Int,
        userType: String,
        requestId: Int?,
        date: String,
        info: String,
    ) = helper.insert(userId, userType, requestId, date, info)
    suspend fun update(
        id: Int,
        requestId: Int?,
        date: String,
        info: String,
    ) = helper.update(id, requestId, date, info)
    suspend fun delete(id: Int) = helper.delete(id)
}