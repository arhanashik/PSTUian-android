package com.workfort.pstuian.repository

import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.networking.CheckInApiHelper

/**
 *  ****************************************************************************
 *  * Created by : arhan on 13 Dec, 2021 at 12:25.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class CheckInRepository(
    private val authRepo: AuthRepository,
    private val helper: com.workfort.pstuian.networking.CheckInApiHelper,
) {
    suspend fun getAll(locationId: Int, page: Int) = helper.getAll(locationId = locationId, page = page)
    suspend fun getAll(userId: Int, userType: String, page: Int) =
        helper.getAll(userId = userId, userType = userType, page = page)
    suspend fun getMyCheckIn() : CheckInEntity {
        val userIdAndType = authRepo.getUserIdAndType()

        return helper.getMyCheckIn(userIdAndType.first, userIdAndType.second)
            ?: throw Exception("Failed")
    }
    suspend fun checkIn(locationId: Int) : CheckInEntity {
        val userIdAndType = authRepo.getUserIdAndType()

        return helper.checkIn(locationId, userIdAndType.first, userIdAndType.second)
            ?: throw Exception("Check in failed")
    }
    suspend fun updatePrivacy(checkInId: Int, privacy: String) =
        helper.updatePrivacy(checkInId, privacy) ?: throw Exception("Update failed")

    suspend fun delete(checkInId: Int) = helper.delete(checkInId)
}