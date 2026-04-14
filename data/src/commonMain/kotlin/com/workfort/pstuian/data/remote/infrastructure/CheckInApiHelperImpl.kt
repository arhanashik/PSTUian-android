package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.dto.CheckInDto
import com.workfort.pstuian.data.remote.domain.CheckInApiHelper
import com.workfort.pstuian.data.remote.service.CheckInApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 13 Dec, 2021 at 12:18.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
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
    ): List<CheckInDto> {
        service.getAllByLocation(locationId, page, limit).also {
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
    ): List<CheckInDto> {
        service.getAllByUser(userId, userType, page, limit).also {
            if(!it.success) throw Exception(it.message)
            return it.data?: throw Exception("No data")
        }
    }

    override suspend fun getMyCheckIn(userId: Int, userType: String): CheckInDto {
        service.get(userId, userType).also {
            if(!it.success) throw Exception(it.message)
            return it.data ?: throw Exception("No data")
        }
    }

    override suspend fun checkIn(
        locationId: Int,
        userId: Int,
        userType: String
    ): CheckInDto {
        service.checkIn(locationId, userId, userType).also {
            if(!it.success) throw Exception(it.message)
            return it.data?: throw Exception("No data")
        }
    }

    override suspend fun updatePrivacy(checkInId: Int, privacy: String): CheckInDto {
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