package com.workfort.pstuian.app.data.repository

import com.workfort.pstuian.app.data.local.blooddonationrequest.BloodDonationRequestEntity
import com.workfort.pstuian.app.data.remote.apihelper.BloodDonationRequestApiHelper

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

class BloodDonationRequestRepository(
    private val authRepo: AuthRepository,
    private val helper: BloodDonationRequestApiHelper
) {
    suspend fun getAll(page: Int) = helper.getAll(page, limit = 20)
    suspend fun get(id: Int) = helper.get(id)
    suspend fun insert(
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ) : BloodDonationRequestEntity {
        val userIdAndType = authRepo.getUserIdAndType()

        return helper.insert(userIdAndType.first, userIdAndType.second, bloodGroup,
            beforeDate, contact, info)
    }

    suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ) = helper.update(id, bloodGroup, beforeDate, contact, info)
    suspend fun delete(id: Int) = helper.delete(id)
}