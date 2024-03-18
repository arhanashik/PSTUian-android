package com.workfort.pstuian.repository

import com.workfort.pstuian.model.BloodDonationRequestEntity
import com.workfort.pstuian.networking.BloodDonationRequestApiHelper

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 1:50 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class BloodDonationRequestRepository(
    private val authRepo: AuthRepository,
    private val helper: com.workfort.pstuian.networking.BloodDonationRequestApiHelper,
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