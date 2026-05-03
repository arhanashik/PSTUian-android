package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.ApiResponseCode
import com.workfort.pstuian.data.model.CheckInDto
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.CheckInApiHelper
import com.workfort.pstuian.data.remote.service.CheckInApiService

class CheckInApiHelperImpl(private val service: CheckInApiService) : CheckInApiHelper {

    override suspend fun getAll(
        locationId: Int,
        page: Int,
        limit: Int,
    ): NetworkResult<List<CheckInDto>> {
        return runCatching {
            service.getAllByLocation(locationId, page, limit).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun getHistory(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int,
    ): NetworkResult<List<CheckInDto>> {
        return runCatching {
            service.getHistory(userId, userType, page, limit).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun getCheckIn(userId: Int, userType: String): NetworkResult<CheckInDto> {
        return runCatching {
            service.get(userId, userType).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun checkIn(
        locationId: Int,
        userId: Int,
        userType: String,
    ): NetworkResult<Unit> {
        return runCatching {
            service.checkIn(locationId, userId, userType).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun updatePrivacy(checkInId: Int, privacy: String): NetworkResult<Unit> {
        return runCatching {
            service.updatePrivacy(checkInId, privacy).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun delete(id: Int): NetworkResult<Unit> {
        return runCatching {
            service.delete(id).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }
}