package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.model.CheckInDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.CheckInApiHelper
import com.workfort.pstuian.data.remote.safeApiCall
import com.workfort.pstuian.data.remote.service.CheckInApiService

class CheckInApiHelperImpl(private val service: CheckInApiService) : CheckInApiHelper {

    override suspend fun getAll(
        locationId: Int,
        page: Int,
        limit: Int,
    ): NetworkResult<List<CheckInDto>> {
        return safeApiCall { service.getAllByLocation(locationId, page, limit) }
    }

    override suspend fun getHistory(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int,
    ): NetworkResult<List<CheckInDto>> {
        return safeApiCall { service.getHistory(userId, userType, page, limit) }
    }

    override suspend fun getCheckIn(userId: Int, userType: String): NetworkResult<CheckInDto> {
        return safeApiCall { service.get(userId, userType) }
    }

    override suspend fun checkIn(
        locationId: Int,
        userId: Int,
        userType: String,
    ): NetworkResult<Unit> {
        return safeApiCall { service.checkIn(locationId, userId, userType) }
    }

    override suspend fun updatePrivacy(checkInId: Int, privacy: String): NetworkResult<Unit> {
        return safeApiCall { service.updatePrivacy(checkInId, privacy) }
    }

    override suspend fun delete(id: Int): NetworkResult<Unit> {
        return safeApiCall { service.delete(id) }
    }
}
