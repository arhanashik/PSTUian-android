package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.checkin.CheckInEntity
import com.workfort.pstuian.util.remote.CheckInApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 13 Dec, 2021 at 12:18.
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

class CheckInApiHelperImpl(private val service: CheckInApiService) : CheckInApiHelper() {
    /**
     * Get all check in by location
     * */
    override suspend fun getAll(
        locationId: Int,
        page: Int,
        limit: Int
    ): List<CheckInEntity> {
        service.getAll(locationId, page, limit).also {
            if(!it.success) throw Exception(it.message)
            return it.data?: throw Exception("No data")
        }
    }

    /**
     * Get all check in by user
     * */
    override suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int
    ): List<CheckInEntity> {
        service.getAll(userId, userType, page, limit).also {
            if(!it.success) throw Exception(it.message)
            return it.data?: throw Exception("No data")
        }
    }

    override suspend fun getMyCheckIn(userId: Int, userType: String): CheckInEntity {
        service.get(userId, userType).also {
            if(!it.success) throw Exception(it.message)
            return it.data?: throw Exception("No data")
        }
    }

    override suspend fun checkIn(
        locationId: Int,
        userId: Int,
        userType: String
    ): CheckInEntity {
        service.checkIn(locationId, userId, userType).also {
            if(!it.success) throw Exception(it.message)
            return it.data?: throw Exception("No data")
        }
    }

    override suspend fun updatePrivacy(checkInId: Int, privacy: String): CheckInEntity {
        service.updatePrivacy(checkInId, privacy).also {
            if(!it.success) throw Exception(it.message)
            return it.data?: throw Exception("No data")
        }
    }

    override suspend fun delete(id: Int): Boolean {
        service.delete(id).also {
            if(!it.success) throw Exception(it.message)
            return true
        }
    }
}