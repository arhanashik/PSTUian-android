package com.workfort.pstuian.app.data.repository

import com.workfort.pstuian.app.data.local.checkin.CheckInEntity
import com.workfort.pstuian.app.data.remote.apihelper.CheckInApiHelper

/**
 *  ****************************************************************************
 *  * Created by : arhan on 13 Dec, 2021 at 12:25.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/13.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class CheckInRepository(
    private val authRepo: AuthRepository,
    private val helper: CheckInApiHelper
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
    suspend fun updateVisibility(visibility: String) : CheckInEntity {
        val userIdAndType = authRepo.getUserIdAndType()

        return helper.updateVisibility(userIdAndType.first, userIdAndType.second, visibility)
            ?: throw Exception("Update failed")
    }
}