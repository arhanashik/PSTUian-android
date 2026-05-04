package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.ApiResponseCode
import com.workfort.pstuian.data.model.BloodDonationRequestDto
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.BloodDonationRequestApiHelper
import com.workfort.pstuian.data.remote.service.BloodDonationRequestApiService

class BloodDonationRequestApiHelperImpl(
    private val service: BloodDonationRequestApiService
) : BloodDonationRequestApiHelper() {

    override suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int,
    ): NetworkResult<List<BloodDonationRequestDto>> {
        return runCatching {
            service.getAll(userId, userType, page, limit).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun get(id: Int): NetworkResult<BloodDonationRequestDto> {
        return runCatching {
            service.get(id).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun insert(
        userId: Int,
        userType: String,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?
    ): NetworkResult<Unit> {
        return runCatching {
            service.insert(userId, userType, bloodGroup, beforeDate, contact, info).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String
    ): NetworkResult<Unit> {
        return runCatching {
            service.update(id, bloodGroup, beforeDate, contact, info).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun markAsComplete(id: Int, userId: Int, userType: String): NetworkResult<Unit> {
        return runCatching {
            service.markAsComplete(id, userId, userType).toNetworkResult()
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